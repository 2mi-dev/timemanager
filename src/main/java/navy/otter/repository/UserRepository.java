package navy.otter.repository;

import navy.otter.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Optional<User> findOneByActivationKey(String activationKey);

  List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

  Optional<User> findOneByResetKey(String resetKey);

  Optional<User> findOneByEmailIgnoreCase(String email);

  Optional<User> findOneById(Long id);

  Optional<User> findOneByUsername(String username);

  @EntityGraph(attributePaths = "authorities")
  Optional<User> findOneWithAuthoritiesById(Long id);

  @EntityGraph(attributePaths = "authorities")
  Optional<User> findOneWithAuthoritiesByUsername(String username);

  Page<User> findAllByUsernameNot(Pageable pageable, String username);

}
