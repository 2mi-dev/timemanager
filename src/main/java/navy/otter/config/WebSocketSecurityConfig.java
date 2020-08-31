package navy.otter.config;

import navy.otter.security.AuthorityConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages
      .simpDestMatchers("/user/queue/errors").permitAll()
      .simpDestMatchers("/topic/tracker").hasAuthority(AuthorityConstants.ADMIN)
      .simpDestMatchers("/topic/**").permitAll()
      .anyMessage().permitAll();
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }
}

