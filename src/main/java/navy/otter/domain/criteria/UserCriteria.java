package navy.otter.domain.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserCriteria {

  private String filter;

  private String username;

  private String nickname;

  private String name;

  private String email;

  private Boolean enabled;

  private Boolean activated;

  private String authority;

}

