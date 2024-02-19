package com.sampledashboard1.config.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sampledashboard1.utils.MethodUtils;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    protected Long id;
    @CreatedDate
    @JsonFormat(pattern = VariableUtils.DATE_TIME_FORMAT)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = VariableUtils.DATE_TIME_FORMAT)
    protected LocalDateTime updatedAt;


    @CreatedBy
    protected U createdBy;

    @LastModifiedBy
    protected U lastModifiedBy;

    protected Boolean isActive;

    @PrePersist
    protected void prePersist() {
        if (MethodUtils.isObjectisNullOrEmpty(this.isActive)) {
            this.setIsActive(Boolean.TRUE);
        }
    }

}



