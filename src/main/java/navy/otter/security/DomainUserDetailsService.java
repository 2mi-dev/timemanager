package navy.otter.security;


import navy.otter.domain.User;
import navy.otter.repository.UserRepository;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public DomainUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("Authenticating {}", username);
    String lowercaseUsername = username.toLowerCase(Locale.ENGLISH);
    Optional<User> userFromDatabase = userRepository
      .findOneWithAuthoritiesByUsername(lowercaseUsername);
    return userFromDatabase.map(user -> {
      if (!user.isActivated()) {
        throw new UserNotActivatedException("User " + lowercaseUsername + " was not activated");
      }
      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
        .map(authority -> new SimpleGrantedAuthority(authority))
        .collect(Collectors.toList());
      return new org.springframework.security.core.userdetails.User(lowercaseUsername,
        user.getPassword(),
        grantedAuthorities);
    }).orElseThrow(() -> new UsernameNotFoundException(
      "User " + lowercaseUsername + " was not found in the database"));
  }
}
