package com.apex.template.domain;

import com.apex.template.common.enums.TaskPriority;
import com.apex.template.common.enums.TaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task extends BaseEntity {
    @NotNull
    private String title;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @NotNull
    private LocalDate startDate,dueDate;
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NEW;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;
}
