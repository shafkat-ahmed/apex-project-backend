package com.apex.template.repository;

import com.apex.template.common.enums.TaskStatus;
import com.apex.template.domain.Task;
import com.apex.template.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {
    Task findFirstByIdAndIsDeletedFalse(Long id);

    List<Task> findAllByIsDeletedFalse();

    Long countAllByCreatedByLogIdAndIsDeletedFalse(Long userId);
    Long countAllByStatusAndIsDeletedFalse(TaskStatus status);
    Long countAllByStatusAndCreatedByLogIdAndIsDeletedFalse(TaskStatus status, Long userId);

    Long countAllByAssignee_IdAndIsDeletedFalse(Long userId);
    Long countAllByStatusAndAssignee_IdAndIsDeletedFalse(TaskStatus status, Long userId);

    @Query("SELECT t FROM Task t " +
            "WHERE t.isDeleted = false " +
            "AND t.lastUpdatedDateTime BETWEEN :fromDate AND :toDate " +
            "AND (t.createdByLogId = :userId OR t.assignee.id = :userId) " +
            "ORDER BY t.lastUpdatedDateTime DESC")
    List<Task> findAllByLastUpdatedDateTimeDesc(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("userId") Long userId
    );

    @Query("SELECT t FROM Task t " +
            "WHERE t.isDeleted = false " +
            "AND t.lastUpdatedDateTime BETWEEN :fromDate AND :toDate " +
            "AND (t.createdByLogId = :userId OR t.assignee.id = :userId) " +
            "ORDER BY t.lastUpdatedDateTime ASC")
    List<Task> findAllByLastUpdatedDateTimeAsc(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("userId") Long userId
    );
}
