package com.apex.template.domain.dto;

import com.apex.template.common.enums.TaskPriority;
import com.apex.template.common.enums.TaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TaskDto {
    private Long id;
    private String title, description;
    private LocalDate startDate, dueDate;
    private String status;
    private String priority;

    private Long assigneeId;
}
