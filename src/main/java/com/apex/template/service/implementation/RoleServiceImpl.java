package com.apex.template.service.implementation;

import com.apex.template.domain.Role;
import com.apex.template.repository.RoleRepository;
import com.apex.template.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleById(long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findFirstByName(name);
    }
}