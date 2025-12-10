package com.apex.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.apex.template.common.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id",nullable=false)
    private Long id;

    private boolean isDeleted;

    @CreatedBy
    private Long createdByLogId;

    @LastModifiedBy
    private Long lastModifiedByLogId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDateTimeLog;

    @LastModifiedDate
    private LocalDateTime lastUpdatedDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private Date lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    @PrePersist
    public void setCreated() {
        this.created = new Date();
        this.lastUpdated = created;
    }

    public void setCreated(Date createdDate) {
        this.created = createdDate;
    }

    public Date getLastUpdated() {
        return lastUpdated == null ? created : lastUpdated;
    }

    @PreUpdate
    public void setLastUpdated() {
        this.lastUpdated = new Date();
    }

    public String getReadableDate(Date date) {
        return DateUtil.getReadableDateForGraph().format(date);
    }

    @JsonIgnore
    public String getReadableCreatedDate() {
        return DateUtil.getReadableDateForView().format(getLastUpdated());
    }
    @JsonIgnore
    public String getReadableDateTime(Date date) {
        return DateUtil.getReadableDateTime().format(date);
    }

}