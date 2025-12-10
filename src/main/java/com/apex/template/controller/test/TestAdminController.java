package com.apex.template.controller.test;

import com.apex.template.common.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/test")
public class TestAdminController {
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception{
        if (id == null){
            throw new EntityNotFoundException("null id received.");
        }
        System.out.println("admin test get called "+id);
        return ResponseEntity.ok().body(id);
    }
}
