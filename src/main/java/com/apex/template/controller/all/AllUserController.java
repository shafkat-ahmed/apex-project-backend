package com.apex.template.controller.all;

import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.domain.dto.UserDTO;
import com.apex.template.service.UserService;
import com.apex.template.service.mapping.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/all/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AllUserController {
    private final UserService service;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserDTO dto) throws Exception{
        if (dto == null){
            throw new EntityNotFoundException("null dto received.");
        }
        return ResponseEntity.ok().body(service.registerUser(dto));
    }
}
