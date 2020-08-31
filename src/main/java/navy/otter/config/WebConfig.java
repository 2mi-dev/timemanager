package navy.otter.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * WebMVC configuration.
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final Environment env;

  private final ApplicationProperties applicationProperties;

  public WebConfig(Environment env, ApplicationProperties applicationProperties) {

    this.env = env;

    this.applicationProperties = applicationProperties;
  }


  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
      .addResourceHandler("swagger-ui.html")
      .addResourceLocations("classpath:/META-INF/resources/");

    registry
      .addResourceHandler("/webjars/**")
      .addResourceLocations("classpath:/META-INF/resources/webjars/");

    registry.addResourceHandler("/**/*")
      .addResourceLocations("classpath:/static/")
      .resourceChain(true)
      .addResolver(new PathResourceResolver() {
        @Override
        protected Resource getResource(String resourcePath,
          Resource location) throws IOException {
          Resource requestedResource = location.createRelative(resourcePath);
          return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
            : new ClassPathResource("/static/index.html");
        }
      });
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**");
  }

//  @Bean
//  ErrorViewResolver supportPathBasedLocationStrategyWithoutHashes() {
//    return (HttpServletRequest request, HttpStatus status,
//      Map<String, Object> model) -> status == HttpStatus.NOT_FOUND
//      ? new ModelAndView("index.html", Collections.emptyMap(), HttpStatus.OK)
//      : null;
//  }

}
