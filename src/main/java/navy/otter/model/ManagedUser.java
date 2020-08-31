package navy.otter.model;

import navy.otter.config.ApplicationConstants;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ManagedUser extends UserModel {

  @Size(min = ApplicationConstants.PASSWORD_MIN_LENGTH, max = ApplicationConstants.PASSWORD_MAX_LENGTH)
  private String password;

}
