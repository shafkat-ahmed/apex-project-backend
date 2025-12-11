package com.apex.template.service.implementation;

import com.apex.template.common.Constants;
import com.apex.template.common.enums.TaskPriority;
import com.apex.template.common.enums.TaskStatus;
import com.apex.template.domain.BaseEntity;
import com.apex.template.domain.User;
import com.apex.template.domain.dto.DashboardDto;
import com.apex.template.domain.dto.PageableResponseDto;
import com.apex.template.service.TaskService;

import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.domain.Task;
import com.apex.template.domain.dto.TaskDto;
import com.apex.template.repository.TaskRepository;
import com.apex.template.service.UserService;
import com.apex.template.service.mapping.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Override
    public TaskDto create(TaskDto dto) throws Exception {
        return taskMapper.responseMapper.apply(
                this.create(taskMapper.requestMapper.apply(dto))
        );
    }

    @Override
    public TaskDto update(TaskDto dto) throws Exception {
        return taskMapper.responseMapper.apply(
                this.update(taskMapper.requestMapper.apply(dto))
        );
    }

    @Override
    public TaskDto readDto(Long id) throws Exception {
        return taskMapper.responseMapper.apply(
                this.read(id)
        );
    }

    public TaskDto generateDto(Task t) throws Exception {
        return taskMapper.responseMapper.apply(t);
    }

    @Override
    public Task create(Task task) throws Exception {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) throws Exception {
        if (task.getId() == null) {
            throw new EntityNotFoundException("id can't be null");
        }
        return taskRepository.save(task);
    }

    @Override
    public Task read(Long id) throws Exception {
        return taskRepository.findFirstByIdAndIsDeletedFalse(id);
    }

    @Override
    public void delete(Long id) throws Exception {
        Task task = this.read(id);
        task.setDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public List<TaskDto> getAll() {
        return taskRepository.findAllByIsDeletedFalse()
                .stream()
                .map(taskMapper.responseMapper)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto updateStatusToNext(Long id) throws Exception {
        Task task = this.read(id);
        if(!task.getStatus().equals(TaskStatus.COMPLETED)) {
            int newStatus = task.getStatus().ordinal() + 1;
            task.setStatus(TaskStatus.values()[newStatus]);
            this.update(task);
        }
        return this.generateDto(task);
    }

    @Override
    public TaskDto assignTaskToUser(Long taskId, Long userId) throws Exception {
        Task task = this.read(taskId);
        User user = userService.read(userId);
        task.setAssignee(user);
        this.update(task);
        return generateDto(task);
    }

    @Override
    public PageableResponseDto<TaskDto> findAllPaginatedByFilters(
            String status,
            String priority,
            String keyword,
            Integer pageNumber,
            Integer pageLength) throws Exception {

        Pageable page = PageRequest.of(pageNumber, pageLength);

        // --- Normalize filters ---
         TaskStatus taskStatus = null;
         TaskPriority taskPriority = null;

        if (status != null && !status.trim().isEmpty()) {
            taskStatus = TaskStatus.valueOf(status.trim());
        }

        if (priority != null && !priority.trim().isEmpty()) {
            taskPriority = TaskPriority.valueOf(priority.trim());
        }

        String keywordFilter = (keyword == null || keyword.trim().isEmpty())
                ? null
                : keyword.trim().toLowerCase();

        final TaskStatus finalTaskStatus = taskStatus;
        final TaskPriority finalTaskPriority = taskPriority;
        User currentUser = userService.getCurrentLoggedUser();

        // --- Query DB ---
        Page<Task> pageResult = taskRepository.findAll((root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Soft delete check
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.equal(root.get("createdByLogId"), currentUser.getId()));

            // Filter: status
            if (finalTaskStatus != null) {
                predicates.add(cb.equal(root.get("status"), finalTaskStatus));
            }

            // Filter: priority
            if (finalTaskPriority != null) {
                predicates.add(cb.equal(root.get("priority"), finalTaskPriority));
            }

            // Filter: keyword in title (case insensitive)
            if (keywordFilter != null) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + keywordFilter + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        }, page);

        // --- Prepare response ---
        PageableResponseDto<TaskDto> responseDto = new PageableResponseDto<>();
        responseDto.setTotalNumberOfElements(pageResult.getTotalElements());
        responseDto.setData(
                pageResult.getContent()
                        .stream()
                        .map(taskMapper.responseMapper)
                        .collect(Collectors.toList())
        );

        return responseDto;
    }

    @Override
    public PageableResponseDto<TaskDto> findAllAssignedPaginatedByFilters(String status, String priority, String keyword,
                                                                          Integer pageNumber, Integer pageLength) throws Exception {

        Pageable page = PageRequest.of(pageNumber, pageLength);

        // --- Normalize filters ---
        TaskStatus taskStatus = null;
        TaskPriority taskPriority = null;

        if (status != null && !status.trim().isEmpty()) {
            taskStatus = TaskStatus.valueOf(status.trim());
        }

        if (priority != null && !priority.trim().isEmpty()) {
            taskPriority = TaskPriority.valueOf(priority.trim());
        }

        String keywordFilter = (keyword == null || keyword.trim().isEmpty())
                ? null
                : keyword.trim().toLowerCase();

        final TaskStatus finalTaskStatus = taskStatus;
        final TaskPriority finalTaskPriority = taskPriority;
        User currentUser = userService.getCurrentLoggedUser();

        // --- Query DB ---
        Page<Task> pageResult = taskRepository.findAll((root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Soft delete check
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.equal(root.get("assignee").get("id"), currentUser.getId()));

            // Filter: status
            if (finalTaskStatus != null) {
                predicates.add(cb.equal(root.get("status"), finalTaskStatus));
            }

            // Filter: priority
            if (finalTaskPriority != null) {
                predicates.add(cb.equal(root.get("priority"), finalTaskPriority));
            }

            // Filter: keyword in title (case insensitive)
            if (keywordFilter != null) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + keywordFilter + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        }, page);

        // --- Prepare response ---
        PageableResponseDto<TaskDto> responseDto = new PageableResponseDto<>();
        responseDto.setTotalNumberOfElements(pageResult.getTotalElements());
        responseDto.setData(
                pageResult.getContent()
                        .stream()
                        .map(taskMapper.responseMapper)
                        .collect(Collectors.toList())
        );

        return responseDto;
    }

    @Override
    public DashboardDto getDashboardData() throws Exception {
        DashboardDto dashboardDto = new DashboardDto();
        User user = userService.getCurrentLoggedUser();
        dashboardDto.setTotalTasks(taskRepository.countAllByCreatedByLogIdAndIsDeletedFalse(user.getId()));
        dashboardDto.setPendingTasks(taskRepository.countAllByStatusAndCreatedByLogIdAndIsDeletedFalse(
                TaskStatus.NEW,user.getId()
        ));
        dashboardDto.setOngoingTasks(taskRepository.countAllByStatusAndCreatedByLogIdAndIsDeletedFalse(
                TaskStatus.IN_PROGRESS,user.getId()
        ));
        dashboardDto.setCompletedTasks(taskRepository.countAllByStatusAndCreatedByLogIdAndIsDeletedFalse(
                TaskStatus.COMPLETED,user.getId()
        ));

        if(user.getRole().getName().equals(Constants.Role.USER)) {
            dashboardDto.setTotalAssignedTasks(taskRepository.countAllByAssignee_IdAndIsDeletedFalse(user.getId()));
            dashboardDto.setPendingAssignedTasks(taskRepository.countAllByStatusAndAssignee_IdAndIsDeletedFalse
                    (TaskStatus.NEW,user.getId()));
            dashboardDto.setOngoingAssignedTasks(taskRepository.countAllByStatusAndAssignee_IdAndIsDeletedFalse
                    (TaskStatus.IN_PROGRESS,user.getId()));
            dashboardDto.setCompletedAssignedTasks(taskRepository.countAllByStatusAndAssignee_IdAndIsDeletedFalse
                    (TaskStatus.COMPLETED,user.getId()));
        }

        return dashboardDto;
    }

    @Override
    public List<TaskDto> recentlyUpdatedTasks(LocalDateTime toDate, String sortType) throws Exception {
        LocalDateTime fromDate = toDate.minusDays(2);
        User currentUser = userService.getCurrentLoggedUser();

        List<Task> tasks = sortType.equals("desc")?
                taskRepository.findAllByLastUpdatedDateTimeDesc(fromDate,toDate,currentUser.getId())
                :
                taskRepository.findAllByLastUpdatedDateTimeAsc(fromDate,toDate,currentUser.getId());

        return tasks.stream().map(taskMapper.responseMapper).collect(Collectors.toList());
    }

}

