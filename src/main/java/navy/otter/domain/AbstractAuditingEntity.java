package navy.otter.domain;


import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@JsonInclude(Include.NON_NULL)
@Data
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity<PK extends Serializable> extends AbstractPersistable<PK> {


  @CreatedBy
  @Column(name = "created_by", nullable = false, length = 50, updatable = false)
  @JsonIgnore
  private String createdBy;

  @CreatedDate
  @Column(name = "created_date", nullable = false)
  @JsonIgnore
  private Instant createdDate = Instant.now();

  @LastModifiedBy
  @Column(name = "last_modified_by", length = 50)
  @JsonIgnore
  private String lastModifiedBy;

  @LastModifiedDate
  @Column(name = "last_modified_date")
  @JsonIgnore
  private Instant lastModifiedDate = Instant.now();


}
