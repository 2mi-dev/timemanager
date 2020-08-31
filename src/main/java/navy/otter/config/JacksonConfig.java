package navy.otter.config;


import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

/**
 * Jackson configuration.
 */
@Configuration
public class JacksonConfig {

  /*
   * Support for Hibernate types in Jackson.
   */
  @Bean
  public Hibernate5Module hibernate5Module() {
    return new Hibernate5Module();
  }

  /*
   * Jackson Afterburner module to speed up serialization/deserialization.
   */
  @Bean
  public AfterburnerModule afterburnerModule() {
    return new AfterburnerModule();
  }

  /*
   * Module for serialization/deserialization of RFC7807 Problem.
   */
  @Bean
  public ProblemModule problemModule() {
    return new ProblemModule();
  }

  /*
   * Module for serialization/deserialization of ConstraintViolationProblem.
   */
  @Bean
  public ConstraintViolationProblemModule constraintViolationProblemModule() {
    return new ConstraintViolationProblemModule();
  }

}
