package com.apex.template.controller.user;

import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.domain.dto.TaskDto;
import com.apex.template.service.TaskService;
import com.apex.template.service.UserService;
import com.apex.template.service.mapping.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminManagerController {
    private final UserService service;
    private final TaskService taskService;

    // -----------------------------
    //      ASSIGN TASK TO USER
    // -----------------------------
    @PatchMapping("/assign/user/to/task")
    public ResponseEntity<TaskDto> assignTaskToUser(
            @RequestParam Long taskId,
            @RequestParam Long userId
    ) throws Exception {
        return ResponseEntity.ok(taskService.assignTaskToUser(taskId, userId));
    }

}
