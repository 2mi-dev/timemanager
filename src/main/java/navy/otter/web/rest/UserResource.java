package navy.otter.web.rest;

import com.google.common.collect.ImmutableMap;
import navy.otter.domain.User;
import navy.otter.domain.criteria.UserCriteria;
import navy.otter.mapper.UserMapper;
import navy.otter.model.UserModel;
import navy.otter.model.Validation;
import navy.otter.repository.UserRepository;
import navy.otter.security.AuthorityConstants;
import navy.otter.service.MailService;
import navy.otter.service.UserService;
import navy.otter.web.rest.error.BadRequestAlertException;
import navy.otter.web.rest.error.EmailAlreadyUsedException;
import navy.otter.web.rest.error.UsernameAlreadyUsedException;
import navy.otter.web.rest.util.HeaderUtils;
import navy.otter.web.rest.util.ResponseUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserResource {

  private final UserRepository userRepository;

  private final UserService userService;

  private final UserMapper userMapper;

  private final MailService mailService;

  public UserResource(UserRepository userRepository, UserService userService, UserMapper userMapper,
    MailService mailService) {
    this.userRepository = userRepository;
    this.userService = userService;
    this.userMapper = userMapper;
    this.mailService = mailService;
  }


  /**
   * POST  /users  : Creates a new user. <p> Creates a new user if the login and email are not
   * already used, and sends an mail with an activation link. The user needs to be activated on
   * creation.
   *
   * @param userModel the user to create
   * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status
   * 400 (Bad Request) if the login or email is already in use
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws BadRequestAlertException 400 (Bad Request) if the username or email is already in use
   */
  @PostMapping("/users")
  @Secured(AuthorityConstants.ADMIN)
  public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserModel userModel)
    throws URISyntaxException {
    log.debug("REST request to save User : {}", userModel);

    if (userModel.getId() != null) {
      throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement",
        "idexists");
      // Lowercase the user login before comparing with database
    } else if (userRepository.findOneByUsername(userModel.getUsername().toLowerCase())
      .isPresent()) {
      throw new UsernameAlreadyUsedException();
    } else if (userRepository.findOneByEmailIgnoreCase(userModel.getEmail()).isPresent()) {
      throw new EmailAlreadyUsedException();
    } else {
      User newUser = userService.createUser(userModel);
      //mailService.sendCreationEmail(newUser);
      return ResponseEntity.created(new URI("/api/users/" + newUser.getUsername()))
        .headers(HeaderUtils.createAlert("userManagement.created", newUser.getUsername()))
        .body(userMapper.entityToModel(newUser));
    }
  }

  /**
   * PUT /users : Updates an existing User.
   *
   * @param userModel the user to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated user
   * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
   * @throws UsernameAlreadyUsedException 400 (Bad Request) if the login is already in use
   */
  @PutMapping("/users")
  @Secured(AuthorityConstants.ADMIN)
  public ResponseEntity<UserModel> updateUser(@Valid @RequestBody UserModel userModel) {
    log.debug("REST request to update User : {}", userModel);
    Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userModel.getEmail());
    if (existingUser.isPresent() && (!existingUser.get().getId().equals(userModel.getId()))) {
      throw new EmailAlreadyUsedException();
    }
    existingUser = userRepository.findOneByUsername(userModel.getUsername().toLowerCase());
    if (existingUser.isPresent() && (!existingUser.get().getId().equals(userModel.getId()))) {
      throw new UsernameAlreadyUsedException();
    }
    Optional<UserModel> updatedUser = userService.updateUser(userModel);

    return ResponseUtils.wrapOrNotFound(updatedUser,
      HeaderUtils.createAlert("userManagement.updated", userModel.getUsername()));
  }

  @GetMapping(value = "/users")
  public Page<UserModel> query(@ModelAttribute UserCriteria criteria,
    @PageableDefault Pageable pageable) {
    return userService.query(criteria, pageable);
  }

//  /**
//   * GET /users : get all users.
//   *
//   * @param pageable the pagination information
//   * @return the ResponseEntity with status 200 (OK) and with body all users
//   */
//  @GetMapping("/users")
//  public ResponseEntity<List<UserModel>> getAllUsers(Pageable pageable) {
//    final Page<UserModel> page = userService.getAllManagedUsers(pageable);
//    HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page, "/api/users");
//    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//  }

  /**
   * @return a string list of the all of the roles
   */
  @GetMapping("/users/authorities")
  @Secured(AuthorityConstants.ADMIN)
  public List<String> getAuthorities() {
    return Arrays.asList(AuthorityConstants.ADMIN, AuthorityConstants.USER);
  }

  /**
   * GET /users/:id : get the "id" user.
   *
   * @param id the id of the user to find
   * @return the ResponseEntity with status 200 (OK) and with body the "id" user, or with
   * status 404 (Not Found)
   */
  @GetMapping("/users/{id}")
  public ResponseEntity<UserModel> getUser(@PathVariable Long id) {
    log.debug("REST request to get User : {}", id);
    return ResponseUtils.wrapOrNotFound(
      userService.getUserWithAuthorities(id)
        .map(userMapper::entityToModel));
  }

  /**
   * DELETE /users/:id : delete the "username" User.
   *
   * @param id the id of the user to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/users/{id}")
  @Secured(AuthorityConstants.ADMIN)
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.debug("REST request to delete User: {}", id);
    userService.deleteUser(id);
    return ResponseEntity.ok().headers(HeaderUtils.createAlert("userManagement.deleted", id.toString()))
      .build();
  }


  @PostMapping(value = "/validators/username-not-taken")
  public Map<String,Boolean> validateUsernameNotTaken(@RequestBody Validation validation) {
    return userRepository.findOneByUsername(validation.getValue())
      .filter(u -> validation.getId() == null ? true : !validation.getId().equals(u.getId()))
      .map(user -> ImmutableMap.of("duplicated", true)).orElse(null);
  }

}

