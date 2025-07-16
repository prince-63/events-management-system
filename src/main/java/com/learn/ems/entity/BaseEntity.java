package com.learn.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Schema(description = "Base entity for auditing purposes")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseEntity {

    @Schema(description = "Timestamp of creation", example = "2025-07-01T11:30:00")
    @CreatedDate
    @Column(updatable = false)
    @JsonIgnore
    private LocalDateTime createdAt;

    @Schema(description = "User who created this record", example = "admin@ems.com")
    @CreatedBy
    @Column(updatable = false)
    @JsonIgnore
    private String createdBy;

    @Schema(description = "Timestamp of last update", example = "2025-07-05T15:45:00")
    @LastModifiedDate
    @Column(insertable = false)
    @JsonIgnore
    private LocalDateTime updatedAt;

    @Schema(description = "User who last modified this record", example = "admin@ems.com")
    @LastModifiedBy
    @Column(insertable = false)
    @JsonIgnore
    private String updatedBy;

}
