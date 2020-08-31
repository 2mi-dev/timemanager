package navy.otter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class JwtToken {

  @JsonProperty("id_token")
  private String idToken;

}
