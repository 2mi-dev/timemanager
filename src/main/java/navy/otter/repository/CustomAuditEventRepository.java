package navy.otter.repository;

import navy.otter.config.ApplicationConstants;
import navy.otter.domain.PersistentAuditEvent;
import navy.otter.service.AuditEventConverter;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of Spring Boot's AuditEventRepository.
 */
@Slf4j
@Repository
public class CustomAuditEventRepository implements AuditEventRepository {

  private static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";

  /**
   * Should be the same as in Liquibase migration.
   */
  protected static final int EVENT_DATA_COLUMN_MAX_LENGTH = 255;

  private final PersistentAuditEventRepository persistenceAuditEventRepository;

  private final AuditEventConverter auditEventConverter;


  public CustomAuditEventRepository(PersistentAuditEventRepository persistenceAuditEventRepository,
    AuditEventConverter auditEventConverter) {

    this.persistenceAuditEventRepository = persistenceAuditEventRepository;
    this.auditEventConverter = auditEventConverter;
  }

  @Override
  public List<AuditEvent> find(String principal, Instant after, String type) {
    Iterable<PersistentAuditEvent> persistentAuditEvents =
      persistenceAuditEventRepository.findByPrincipalAndEventDateAfterAndEventType(principal, after, type);
    return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
  }


  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void add(AuditEvent event) {
    if (!AUTHORIZATION_FAILURE.equals(event.getType()) &&
      !ApplicationConstants.ANONYMOUS_USER.equals(event.getPrincipal())) {

      PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
      persistentAuditEvent.setPrincipal(event.getPrincipal());
      persistentAuditEvent.setEventType(event.getType());
      persistentAuditEvent.setEventDate(event.getTimestamp());
      Map<String, String> eventData = auditEventConverter.convertDataToStrings(event.getData());
      persistentAuditEvent.setData(truncate(eventData));
      persistenceAuditEventRepository.save(persistentAuditEvent);
    }
  }

  /**
   * Truncate event data that might exceed column length.
   */
  private Map<String, String> truncate(Map<String, String> data) {
    Map<String, String> results = new HashMap<>();

    if (data != null) {
      for (Map.Entry<String, String> entry : data.entrySet()) {
        String value = entry.getValue();
        if (value != null) {
          int length = value.length();
          if (length > EVENT_DATA_COLUMN_MAX_LENGTH) {
            value = value.substring(0, EVENT_DATA_COLUMN_MAX_LENGTH);
            log.warn("Event data for {} too long ({}) has been truncated to {}. Consider increasing column width.",
              entry.getKey(), length, EVENT_DATA_COLUMN_MAX_LENGTH);
          }
        }
        results.put(entry.getKey(), value);
      }
    }
    return results;
  }
}
