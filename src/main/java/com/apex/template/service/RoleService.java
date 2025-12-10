package com.apex.template.service;

import com.apex.template.domain.Role;

public interface RoleService {

    Role findRoleById(long id);
    Role findRoleByName(String name);

}
