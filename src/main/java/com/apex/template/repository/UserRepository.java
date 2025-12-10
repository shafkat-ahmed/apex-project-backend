package com.apex.template.repository;

import com.apex.template.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    List<User> findAllByRoleIdAndIsDeletedFalse(Long roleId);

    List<User> findAllByRoleIdAndIsDeletedTrue(Long roleId);

    User getFirstById(Long userId);

    User findByIdAndIsDeletedFalse(Long id);

}