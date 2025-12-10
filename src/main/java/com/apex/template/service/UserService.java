package com.apex.template.service;

import com.apex.template.common.exception.UserNotFoundException;
import com.apex.template.domain.User;
import com.apex.template.domain.dto.UserDTO;

import java.util.List;

public interface UserService extends EntityGenericCrudService<User> {

    UserDTO registerUser(UserDTO userDTO) throws Exception;

    User readByUsername(String username) throws UserNotFoundException;

    boolean isPasswordMatches(User user, String password) throws Exception;


    UserDTO update(UserDTO dto) throws Exception;

    UserDTO readDTO(Long id) throws Exception;

    List<UserDTO> findUsersByRoleId(Long roleId, String status) throws Exception;

    User getCurrentLoggedUser() throws Exception;

    List<UserDTO> findUsersByRoleName(String roleName) throws Exception;
}