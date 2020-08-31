package navy.otter.model;

import navy.otter.config.ApplicationConstants;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {

  @Pattern(regexp = ApplicationConstants.USERNAME_REGEX)
  @NotNull
//  @Size(min = 1, max = 50)
  private String username;

  @NotNull
//  @Size(min = ApplicationConstants.PASSWORD_MIN_LENGTH, max = ApplicationConstants.PASSWORD_MAX_LENGTH)
  private String password;

  private Boolean rememberMe;

}
