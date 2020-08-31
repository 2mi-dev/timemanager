package navy.otter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Data
@Entity
@Table(name = "persistent_audit_event")
public class PersistentAuditEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "event_id")
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String principal;

  @Column(name = "event_date")
  private Instant eventDate;

  @Column(name = "event_type")
  private String eventType;

  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "persistent_audit_event_data", joinColumns = @JoinColumn(name = "event_id"))
  private Map<String, String> data = new HashMap<>();

}
