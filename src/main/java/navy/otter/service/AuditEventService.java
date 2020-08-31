package navy.otter.service;

import navy.otter.repository.PersistentAuditEventRepository;
import java.time.Instant;
import java.util.Optional;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 */
@Service
@Transactional
public class AuditEventService {

  private final PersistentAuditEventRepository persistenceAuditEventRepository;

  private final AuditEventConverter auditEventConverter;

  public AuditEventService(
    PersistentAuditEventRepository persistenceAuditEventRepository,
    AuditEventConverter auditEventConverter) {

    this.persistenceAuditEventRepository = persistenceAuditEventRepository;
    this.auditEventConverter = auditEventConverter;
  }

  public Page<AuditEvent> findAll(Pageable pageable) {
    return persistenceAuditEventRepository.findAll(pageable)
      .map(auditEventConverter::convertToAuditEvent);
  }

  public Page<AuditEvent> findByDates(Instant fromDate, Instant toDate, Pageable pageable) {
    return persistenceAuditEventRepository.findAllByEventDateBetween(fromDate, toDate, pageable)
      .map(auditEventConverter::convertToAuditEvent);
  }

  public Optional<AuditEvent> find(Long id) {
    return persistenceAuditEventRepository.findById(id).map
      (auditEventConverter::convertToAuditEvent);
  }
}

