package com.apex.template.controller.task;

import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.domain.dto.DashboardDto;
import com.apex.template.domain.dto.PageableResponseDto;
import com.apex.template.domain.dto.TaskDto;
import com.apex.template.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskUserController {

    private final TaskService taskService;

    // -----------------------------
    //        CREATE TASK
    // -----------------------------
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto dto) throws Exception {
        return ResponseEntity.ok(taskService.create(dto));
    }

    // -----------------------------
    //        UPDATE TASK
    // -----------------------------
    @PutMapping()
    public ResponseEntity<TaskDto> updateTask(
            @RequestBody TaskDto dto
    ) throws Exception {
        return ResponseEntity.ok(taskService.update(dto));
    }

    // -----------------------------
    //        DELETE TASK
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws Exception {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------
    //        GET ALL
    // -----------------------------
    @GetMapping()
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAll());
    }

    // -----------------------------
    //        GET BY ID
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) throws Exception {
        if(id == null) {
            throw new EntityNotFoundException("Task id is null");
        }
        return ResponseEntity.ok(taskService.readDto(id));
    }

    // -----------------------------
    //   PAGINATED + FILTERED TASKS
    // -----------------------------
    @GetMapping("/by/filters/paginated")
    public ResponseEntity<PageableResponseDto<TaskDto>> getTasksPaginated(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageLength
    ) throws Exception {
        return ResponseEntity.ok(
                taskService.findAllPaginatedByFilters(status, priority, keyword, pageNumber, pageLength)
        );
    }

    // -----------------------------
    //   PAGINATED ASSIGNED TASKS
    // -----------------------------
    @GetMapping("/assigned")
    public ResponseEntity<PageableResponseDto<TaskDto>> getAssignedTasksPaginated(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageLength
    ) throws Exception {
        return ResponseEntity.ok(
                taskService.findAllAssignedPaginatedByFilters(
                        status, priority, keyword, pageNumber, pageLength
                )
        );
    }

    // -----------------------------
    //      UPDATE STATUS TO NEXT
    // -----------------------------
    @PatchMapping("/update/status/next/{id}")
    public ResponseEntity<TaskDto> updateStatusToNext(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(taskService.updateStatusToNext(id));
    }

    // -----------------------------
    //        DASHBOARD DATA
    // -----------------------------
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboardData() throws Exception {
        return ResponseEntity.ok(taskService.getDashboardData());
    }
}

