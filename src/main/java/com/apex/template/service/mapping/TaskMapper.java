package com.apex.template.service.mapping;

import com.apex.template.common.enums.TaskPriority;
import com.apex.template.common.enums.TaskStatus;
import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.common.util.RepositoryUtilityMethod;
import com.apex.template.domain.Task;
import com.apex.template.domain.User;
import com.apex.template.domain.dto.TaskDto;
import com.apex.template.repository.TaskRepository;
import com.apex.template.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskMapper {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Function<TaskDto, Task> requestMapper = this::entity;
    public Function<Task, TaskDto> responseMapper = this::dto;

    private Task entity(TaskDto dto) throws EntityNotFoundException {
        Task entity;

        if (dto.getId() != null) {
            entity = RepositoryUtilityMethod.getObject(dto.getId(), taskRepository);
        } else {
            entity = new Task();
        }

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setDueDate(dto.getDueDate());

        if(dto.getStatus() != null) {
            entity.setStatus(TaskStatus.valueOf(dto.getStatus()));
        }
        if(dto.getPriority() != null) {
            entity.setPriority(TaskPriority.valueOf(dto.getPriority()));
        }

        // Assignee mapping
        if (dto.getAssigneeId() != null) {
            User assignee = RepositoryUtilityMethod.getObject(dto.getAssigneeId(), userRepository);
            entity.setAssignee(assignee);
        }

        return entity;
    }

    private TaskDto dto(Task entity) {
        TaskDto dto = new TaskDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setDueDate(entity.getDueDate());

        if (entity.getStatus() != null) {
            dto.setStatus(entity.getStatus().name());
        }

        if (entity.getPriority() != null) {
            dto.setPriority(entity.getPriority().name());
        }

        if (entity.getAssignee() != null) {
            dto.setAssigneeId(entity.getAssignee().getId());
        }

        return dto;
    }
}
