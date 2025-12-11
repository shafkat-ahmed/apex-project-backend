package com.apex.template.service;

import com.apex.template.common.enums.TaskStatus;
import com.apex.template.domain.Task;
import com.apex.template.domain.dto.DashboardDto;
import com.apex.template.domain.dto.PageableResponseDto;
import com.apex.template.domain.dto.TaskDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService extends  EntityGenericCrudService<Task>,
DtoGenericCrudService<TaskDto>{
    List<TaskDto> getAll();
    TaskDto updateStatusToNext(Long id) throws Exception;
    PageableResponseDto<TaskDto> findAllPaginatedByFilters(String status,
                                                           String priority,
                                                           String keyword,
                                                           Integer pageNumber, Integer pageLength) throws Exception;
    TaskDto assignTaskToUser(Long taskId, Long userId) throws Exception;
    PageableResponseDto<TaskDto> findAllAssignedPaginatedByFilters(String status,
                                                           String priority,
                                                           String keyword, Integer pageNumber, Integer pageLength) throws Exception;

    DashboardDto getDashboardData() throws Exception;
    List<TaskDto> recentlyUpdatedTasks(LocalDateTime toDate, String sortType) throws Exception;
}
