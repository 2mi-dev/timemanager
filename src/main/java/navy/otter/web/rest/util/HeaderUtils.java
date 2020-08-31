package navy.otter.web.rest.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
@Slf4j
public final class HeaderUtils {

  private static final String APPLICATION_NAME = "application";

  private HeaderUtils() {
  }

  public static HttpHeaders createAlert(String message, String param) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-application-alert", message);
    headers.add("X-application-params", param);
    return headers;
  }

  public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
  }

  public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
    return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
  }

  public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
  }

  public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
    log.error("Entity processing failed, {}", defaultMessage);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-application-error", "error." + errorKey);
    headers.add("X-application-params", entityName);
    return headers;
  }
}
