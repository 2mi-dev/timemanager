package navy.otter.web.rest;

import static navy.otter.config.ApplicationConstants.PASSWORD_MAX_LENGTH;
import static navy.otter.config.ApplicationConstants.PASSWORD_MIN_LENGTH;

import navy.otter.domain.User;
import navy.otter.mapper.UserMapper;
import navy.otter.model.KeyAndPassword;
import navy.otter.model.ManagedUser;
import navy.otter.model.PasswordChange;
import navy.otter.model.UserModel;
import navy.otter.repository.UserRepository;
import navy.otter.security.SecurityUtils;
import navy.otter.service.MailService;
import navy.otter.service.UserService;
import navy.otter.web.rest.error.EmailAlreadyUsedException;
import navy.otter.web.rest.error.EmailNotFoundException;
import navy.otter.web.rest.error.InternalServerErrorException;
import navy.otter.web.rest.error.InvalidPasswordException;
import navy.otter.web.rest.error.UsernameAlreadyUsedException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the current user's account.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AccountResource {


  private final UserRepository userRepository;

  private final UserService userService;

  private final UserMapper userMapper;

  private final MailService mailService;

  public AccountResource(UserRepository userRepository, UserService userService, UserMapper userMapper, MailService mailService) {

    this.userRepository = userRepository;
    this.userService = userService;
    this.userMapper = userMapper;
    this.mailService = mailService;
  }

  /**
   * POST  /register : register the user.
   *
   * @param managedUser the managed user View Model
   * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
   * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
   * @throws UsernameAlreadyUsedException 400 (Bad Request) if the username is already used
   */
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerAccount(@Valid @RequestBody ManagedUser managedUser) {
    if (!checkPasswordLength(managedUser.getPassword())) {
      throw new InvalidPasswordException();
    }
    userRepository.findOneByUsername(managedUser.getUsername().toLowerCase()).ifPresent(u -> {throw new UsernameAlreadyUsedException();});
    userRepository.findOneByEmailIgnoreCase(managedUser.getEmail()).ifPresent(u -> {throw new EmailAlreadyUsedException();});
    User user = userService.registerUser(managedUser, managedUser.getPassword());
    mailService.sendActivationEmail(user);
  }

  /**
   * GET  /activate : activate the registered user.
   *
   * @param key the activation key
   * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
   */
  @GetMapping("/activate")
  public void activateAccount(@RequestParam(value = "key") String key) {
    Optional<User> user = userService.activateRegistration(key);
    if (!user.isPresent()) {
      throw new InternalServerErrorException("No user was found for this reset key");
    }
  }

  /**
   * GET  /authenticate : check if the user is authenticated, and return its username.
   *
   * @param request the HTTP request
   * @return the login if the user is authenticated
   */
  @GetMapping("/authenticate")
  public String isAuthenticated(HttpServletRequest request) {
    log.debug("REST request to check if the current user is authenticated");
    return request.getRemoteUser();
  }

  /**
   * GET  /account : get the current user.
   *
   * @return the current user
   * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
   */
  @GetMapping("/account")
  public UserModel getAccount() {
    return userService.getUserWithAuthorities()
      .map(userMapper::entityToModel)
      .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
  }

  /**
   * POST  /account : update the current user information.
   *
   * @param userModel the current user information
   * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
   * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
   */
  @PostMapping("/account")
  public void saveAccount(@Valid @RequestBody UserModel userModel) {
    final String username = SecurityUtils.getCurrentUsername().orElseThrow(() -> new InternalServerErrorException("Current username not found"));
    Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userModel.getEmail());
    if (existingUser.isPresent() && (!existingUser.get().getUsername().equalsIgnoreCase(username))) {
      throw new EmailAlreadyUsedException();
    }
    Optional<User> user = userRepository.findOneByUsername(username);
    if (!user.isPresent()) {
      throw new InternalServerErrorException("User could not be found");
    }
    userService.updateUser(userModel.getNickname(), userModel.getName(), userModel.getEmail(),userModel.getPhone(),
      userModel.getLangKey(), userModel.getAvatar());
  }

  /**
   * POST  /account/change-password : changes the current user's password
   *
   * @param passwordChange current and new password
   * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
   */
  @PostMapping(path = "/account/change-password")
  public void changePassword(@RequestBody PasswordChange passwordChange) {
    if (!checkPasswordLength(passwordChange.getNewPassword())) {
      throw new InvalidPasswordException();
    }
    userService.changePassword(passwordChange.getOldPassword(),passwordChange.getNewPassword());
  }

  /**
   * POST   /account/reset-password/init : Send an email to reset the password of the user
   *
   * @param mail the mail of the user
   * @throws EmailNotFoundException 400 (Bad Request) if the email address is not registered
   */
  @PostMapping(path = "/account/reset-password/init")
  public void requestPasswordReset(@RequestBody String mail) {
    mailService.sendPasswordResetMail(
      userService.requestPasswordReset(mail)
        .orElseThrow(EmailNotFoundException::new)
    );
  }

  /**
   * POST   /account/reset-password/finish : Finish to reset the password of the user
   *
   * @param keyAndPassword the generated key and the new password
   * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
   * @throws RuntimeException 500 (Internal Server Error) if the password could not be reset
   */
  @PostMapping(path = "/account/reset-password/finish")
  public void finishPasswordReset(@RequestBody KeyAndPassword keyAndPassword) {
    if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
      throw new InvalidPasswordException();
    }
    Optional<User> user =
      userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

    if (!user.isPresent()) {
      throw new InternalServerErrorException("No user was found for this reset key");
    }
  }

  private static boolean checkPasswordLength(String password) {
    return !StringUtils.isEmpty(password) &&
      password.length() >= PASSWORD_MIN_LENGTH &&
      password.length() <= PASSWORD_MAX_LENGTH;
  }
}

