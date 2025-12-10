package com.apex.template.repository;

import com.apex.template.common.enums.TaskStatus;
import com.apex.template.domain.Task;
import com.apex.template.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {
    Task findFirstByIdAndIsDeletedFalse(Long id);

    List<Task> findAllByIsDeletedFalse();

    Long countAllByCreatedByLogIdAndIsDeletedFalse(Long userId);
    Long countAllByStatusAndIsDeletedFalse(TaskStatus status);
    Long countAllByStatusAndCreatedByLogIdAndIsDeletedFalse(TaskStatus status, Long userId);

}
