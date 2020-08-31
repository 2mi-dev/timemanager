package navy.otter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Application configuration properties
 */
@Data
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  private final Mail mail = new Mail();

  private final Security security = new Security();

  private final Logging logging = new Logging();

  @Data
  public static class Mail {

    private String from = ApplicationDefaults.Mail.from;

    private String baseUrl = ApplicationDefaults.Mail.baseUrl;
  }

  @Data
  public static class Security {

    private final Authentication authentication = new Authentication();

    @Data
    public static class Authentication {

      private final Jwt jwt = new Jwt();

      @Data
      public static class Jwt {

        private String secret = ApplicationDefaults.Security.Authentication.Jwt.secret;

        private long tokenValidityInSeconds = ApplicationDefaults.Security.Authentication.Jwt.tokenValidityInSeconds;

        private long tokenValidityInSecondsForRememberMe = ApplicationDefaults.Security.Authentication.Jwt.tokenValidityInSecondsForRememberMe;

      }

    }


  }

  @Data
  public static class Logging {

    private final Logstash logstash = new Logstash();

    @Data
    public static class Logstash {

      private boolean enabled = ApplicationDefaults.Logging.Logstash.enabled;

      private String host = ApplicationDefaults.Logging.Logstash.host;

      private int port = ApplicationDefaults.Logging.Logstash.port;

      private int queueSize = ApplicationDefaults.Logging.Logstash.queueSize;
    }
  }

}
