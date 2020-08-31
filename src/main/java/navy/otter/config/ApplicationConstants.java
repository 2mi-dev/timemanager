package navy.otter.config;


/**
 * Application Constants.
 */
public interface ApplicationConstants {

  // Spring profiles for development
  String SPRING_PROFILE_DEVELOPMENT = "dev";

  // Spring profiles for test
  String SPRING_PROFILE_TEST = "test";

  // Spring profiles for production
  String SPRING_PROFILE_PRODUCTION = "prod";

  // Spring profile used when deploying with Spring Cloud
  String SPRING_PROFILE_CLOUD = "cloud";

  // Spring profile used when deploying to Kubernetes and OpenShift
  String SPRING_PROFILE_K8S = "k8s";

  String USERNAME_REGEX = "^[_'.@A-Za-z0-9-]*$";
  int PASSWORD_MIN_LENGTH = 4;
  int PASSWORD_MAX_LENGTH = 100;

  String SYSTEM_ACCOUNT = "system";

  String ANONYMOUS_USER = "anonymousUser";

  String DEFAULT_LANGUAGE = "zh-cn";


}
