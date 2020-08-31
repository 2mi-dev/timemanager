package navy.otter.config;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Logging configuration.
 */
@Slf4j
@Configuration
public class LoggingConfig {

  private static final String LOGSTASH_APPENDER_NAME = "LOGSTASH";

  private static final String ASYNC_LOGSTASH_APPENDER_NAME = "ASYNC_LOGSTASH";

  private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

  private final String appName;

  private final String serverPort;

  private final ApplicationProperties props;


  public LoggingConfig(@Value("${spring.application.name}") String appName,
    @Value("${server.port}") String serverPort,
    ApplicationProperties props) {
    this.appName = appName;
    this.serverPort = serverPort;
    this.props = props;
    if (props.getLogging().getLogstash().isEnabled()) {
      addLogstashAppender(context);
      addContextListener(context);
    }
//    if (props.getMetrics().getLogs().isEnabled()) {
//      setMetricsMarkerLogbackFilter(context);
//    }
  }


  private void addContextListener(LoggerContext context) {
    LogbackLoggerContextListener loggerContextListener = new LogbackLoggerContextListener();
    loggerContextListener.setContext(context);
    context.addListener(loggerContextListener);
  }

  private void addLogstashAppender(LoggerContext context) {
    log.info("Initializing Logstash logging");

    LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
    logstashAppender.setName(LOGSTASH_APPENDER_NAME);
    logstashAppender.setContext(context);
    String customFields = "{\"app_name\":\"" + appName + "\",\"app_port\":\"" + serverPort + "\"}";

    // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
    LogstashEncoder logstashEncoder = new LogstashEncoder();

    logstashEncoder.setCustomFields(customFields);

    logstashAppender
      .addDestinations(new InetSocketAddress(props.getLogging().getLogstash().getHost(),
        props.getLogging().getLogstash().getPort()));

    ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
    throwableConverter.setRootCauseFirst(true);
    logstashEncoder.setThrowableConverter(throwableConverter);
    logstashEncoder.setCustomFields(customFields);

    logstashAppender.setEncoder(logstashEncoder);
    logstashAppender.start();

    // Wrap the appender in an Async appender for performance
    AsyncAppender asyncLogstashAppender = new AsyncAppender();
    asyncLogstashAppender.setContext(context);
    asyncLogstashAppender.setName(ASYNC_LOGSTASH_APPENDER_NAME);
    asyncLogstashAppender.setQueueSize(props.getLogging().getLogstash().getQueueSize());
    asyncLogstashAppender.addAppender(logstashAppender);
    asyncLogstashAppender.start();

    context.getLogger("ROOT").addAppender(asyncLogstashAppender);
  }


  /**
   * Logback configuration is achieved by configuration file and API. When configuration file change
   * is detected, the configuration is reset. This listener ensures that the programmatic
   * configuration is also re-applied after reset.
   */
  class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {

    @Override
    public boolean isResetResistant() {
      return true;
    }

    @Override
    public void onStart(LoggerContext context) {
      addLogstashAppender(context);
    }

    @Override
    public void onReset(LoggerContext context) {
      addLogstashAppender(context);
    }

    @Override
    public void onStop(LoggerContext context) {
      // Nothing to do.
    }

    @Override
    public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
      // Nothing to do.
    }
  }

}
