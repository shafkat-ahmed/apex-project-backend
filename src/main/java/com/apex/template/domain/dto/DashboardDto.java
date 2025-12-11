package com.apex.template.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DashboardDto {
    private long totalTasks;
    private long completedTasks;
    private long ongoingTasks;
    private long pendingTasks;

    private long totalAssignedTasks;
    private long pendingAssignedTasks;
    private long completedAssignedTasks;
    private long ongoingAssignedTasks;
}
