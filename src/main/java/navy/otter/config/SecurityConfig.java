package navy.otter.config;

import javax.annotation.PostConstruct;
import navy.otter.security.AuthorityConstants;
import navy.otter.security.jwt.JwtConfigurer;
import navy.otter.security.jwt.TokenProvider;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;


/**
 * Security configuration.
 */
@Configuration
@Import(SecurityProblemSupport.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  private final UserDetailsService userDetailsService;

  private final TokenProvider tokenProvider;

  private final SecurityProblemSupport problemSupport;

  public SecurityConfig(
    AuthenticationManagerBuilder authenticationManagerBuilder,
    UserDetailsService userDetailsService,
    TokenProvider tokenProvider,
    SecurityProblemSupport problemSupport) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.userDetailsService = userDetailsService;
    this.tokenProvider = tokenProvider;
    this.problemSupport = problemSupport;
  }

  @PostConstruct
  public void init() {
    try {
      authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
    } catch (Exception e) {
      throw new BeanInitializationException("Security configuration failed", e);
    }
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
      .antMatchers(HttpMethod.OPTIONS, "/**")
      .antMatchers("/app/**/*.{js,html}")
//      .antMatchers("/*.{js,html,map,}")
      .antMatchers("/i18n/**")
      .antMatchers("/assets/**")
      .antMatchers("/content/**")
      .antMatchers("/swagger-resources/**")
      .antMatchers("/swagger-ui.html")
      .antMatchers("/v2/api-docs")
      .antMatchers("/webjars/**")
      .antMatchers("/test/**")
      .antMatchers("/h2-console/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .exceptionHandling()
      .authenticationEntryPoint(problemSupport)
      .accessDeniedHandler(problemSupport)

      .and()

      .csrf()
      .disable()
      .headers()
      .frameOptions()
      .disable()

      .and()

      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

      .and()

      .authorizeRequests()
      .antMatchers("/api/validators/**").permitAll()
      .antMatchers("/api/register").permitAll()
      .antMatchers("/api/activate").permitAll()
      .antMatchers("/api/authenticate").permitAll()
      .antMatchers("/api/account/reset-password/init").permitAll()
      .antMatchers("/api/account/reset-password/finish").permitAll()
      .antMatchers("/api/profile-info").permitAll()
      .antMatchers("/api/**").authenticated()
      .antMatchers("/actuator/**").permitAll()
      .antMatchers("/v2/api-docs").permitAll()
      .antMatchers("/swagger-resources/**").permitAll()
      .antMatchers("/webjars/**").permitAll()

      .and()

      .apply(securityConfigurerAdapter());

  }

  private JwtConfigurer securityConfigurerAdapter() {
    return new JwtConfigurer(tokenProvider);
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }

}
