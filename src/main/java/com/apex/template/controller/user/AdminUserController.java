package com.apex.template.controller.user;

import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.domain.dto.UserDTO;
import com.apex.template.service.UserService;
import com.apex.template.service.mapping.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminUserController {
    private final UserService service;
    private final UserMapper userMapper;


    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO dto) throws Exception{
        if (dto == null){
            throw new EntityNotFoundException("null dto received.");
        }
        return ResponseEntity.ok(service.create(userMapper.requestMapper.apply(dto)));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UserDTO dto) throws Exception{
        if (dto == null){
            throw new EntityNotFoundException("null dto received.");
        }

        return ResponseEntity.ok().body(service.update(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception{
        if (id == null){
            throw new EntityNotFoundException("null id received.");
        }
        return ResponseEntity.ok().body(service.readDTO(id));
    }

    @GetMapping("/get/by/role-id")
    public ResponseEntity<?> getByRoleId(@RequestParam Long roleId, @RequestParam String status) throws Exception{
        if (roleId == null){
            throw new EntityNotFoundException("null id received.");
        }
        return ResponseEntity.ok().body(service.findUsersByRoleId(roleId,status));
    }
    @GetMapping("/get/by/role")
    public ResponseEntity<?> getByRole(@RequestParam String roleName) throws Exception{
        if (roleName == null){
            throw new EntityNotFoundException("null param received.");
        }
        return ResponseEntity.ok().body(service.findUsersByRoleName(roleName));
    }

}
