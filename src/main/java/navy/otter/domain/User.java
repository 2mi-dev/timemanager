package navy.otter.domain;


import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import navy.otter.config.ApplicationConstants;
import java.time.Instant;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "users")
public class User extends AbstractAuditingEntity<Long> {

  @NotNull
  @Pattern(regexp = ApplicationConstants.USERNAME_REGEX)
  @Size(min = 1, max = 50)
  @Column(length = 50, unique = true, nullable = false)
  private String username;

  @JsonIgnore
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "password", length = 100)
  private String password;

  @Size(max = 50)
  @Column(length = 50)
  private String nickname;

  @Size(max = 50)
  @Column(length = 50)
  private String name;

  @Email
  @Size(max = 255)
  @Column(length = 255)
  private String email;

  @Size(max = 255)
  @Column(length = 255)
  private String phone;

  @Size(max = 255)
  @Column(length = 255)
  private String avatar;

  @Size(max = 20)
  @Column(name = "lang_key", length = 20)
  private String langKey;

  @NotNull
  @Column(nullable = false)
  private boolean activated = false;

  @Size(max = 20)
  @Column(name = "activation_key", length = 20)
  @JsonIgnore
  private String activationKey;

  @Size(max = 20)
  @Column(name = "reset_key", length = 20)
  @JsonIgnore
  private String resetKey;

  @Column(name = "reset_date")
  private Instant resetDate = null;

  @Column(nullable = false)
  private boolean enabled = true;

  @ElementCollection(/*fetch = FetchType.EAGER*/)
  @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id") )
  @Column(name = "authority")
  private Set<String> authorities;


}
