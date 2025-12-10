package com.apex.template.repository;

import com.apex.template.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findById(long id);

    Role findFirstByName(String name);
}
