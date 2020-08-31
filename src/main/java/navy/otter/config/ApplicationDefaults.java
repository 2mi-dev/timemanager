package navy.otter.config;

/**
 * Default value for application configuration properties.
 */
public interface ApplicationDefaults {

  interface Mail {

    String from = "";
    String baseUrl = "";

  }

  interface Security {

    interface Authentication {

      interface Jwt {

        String secret = null;
        long tokenValidityInSeconds = 1800; // 0.5 hour
        long tokenValidityInSecondsForRememberMe = 2592000; // 30 hours;
      }
    }

  }

  interface Logging {

    interface Logstash {

      boolean enabled = false;

      String host = "localhost";

      int port = 5000;

      int queueSize = 512;
    }
  }


}
