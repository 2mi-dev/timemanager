package navy.otter.web.rest.error;

import lombok.Data;

@Data
public class FieldError {

  private final String objectName;

  private final String field;

  private final String message;

}
