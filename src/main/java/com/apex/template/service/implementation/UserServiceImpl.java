package com.apex.template.service.implementation;

import com.apex.template.common.Constants;
import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.common.exception.UserNotFoundException;
import com.apex.template.common.util.PasswordUtil;
import com.apex.template.domain.Role;
import com.apex.template.domain.User;
import com.apex.template.domain.dto.UserDTO;
import com.apex.template.repository.UserRepository;
import com.apex.template.service.RoleService;
import com.apex.template.service.UserService;
import com.apex.template.service.mapping.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;


    @Override
    public User create(User user) throws Exception {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws Exception {
        return userRepository.save(user);
    }

    public User read(Long userId) {
        User user = userRepository.getFirstById(userId);
        if (user == null) {
            throw new EntityNotFoundException("No user found with this id: " + userId);
        }
        return user;
    }

    @Override
    public void delete(Long id) throws Exception {
        userRepository.deleteById(id);
    }


    @Override
    public UserDTO registerUser(UserDTO userDTO) throws Exception {
        userDTO.setRoleId(roleService.findRoleByName(Constants.Role.USER).getId());
        if(this.readByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("Username is already in use!");
        }
        User user = this.create(userMapper.requestMapper.apply(userDTO));
        return userMapper.responseMapper.apply(user);
    }

    @Override
    public User readByUsername(String username) throws UserNotFoundException {
        System.out.println("From username in impl:" + username);
        return userRepository.findByUsername(username);
    }


    @Override
    public boolean isPasswordMatches(User user, String password) throws Exception {
        if (PasswordUtil.encryptPassword(password).equals(user.getPassword())) {
            user.setPassword(PasswordUtil.encryptPassword(password));
            user = userRepository.save(user);
        }

        return PasswordUtil.getPasswordEncoder().matches(password, user.getPassword());
    }

    @Override
    public UserDTO update(UserDTO dto) throws Exception {

        if (dto.getId() == null) {
            throw new EntityNotFoundException("id can't be null");
        }

        User user=userMapper.requestMapper.apply(dto);


        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String encryptedPassword = PasswordUtil.encryptPassword(dto.getPassword());

            System.out.println("Encrypted password: " + encryptedPassword);
            user.setPassword(encryptedPassword);
        }
        ;
        return userMapper.responseMapper.apply(userRepository.save(user));


    }
    @Override
    public UserDTO readDTO(Long id) throws Exception {
        return userMapper.responseMapper.apply(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }

    @Override
    public List<UserDTO> findUsersByRoleId(Long roleId, String status) throws Exception {
        if (status.equals("ACTIVE")) {
            return userRepository.findAllByRoleIdAndIsDeletedFalse(roleId).stream()

                    .map(user -> userMapper.responseMapper.apply(user))
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByRoleIdAndIsDeletedTrue(roleId).stream()

                    .map(user -> userMapper.responseMapper.apply(user))
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public User getCurrentLoggedUser() throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof String) {
            username = (String) principal;
        } else if (principal instanceof UserDetails) {
            // Keeping this logic for completeness, though you confirmed String
            username = ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("Principal object is not a String or UserDetails.");
        }

        // This lookup now happens in a brand new, isolated transaction
        User user = userRepository.findByUsername(username);

        return user;
    }

    @Override
    public List<UserDTO> findUsersByRoleName(String roleName) throws Exception {
        Role role = roleService.findRoleByName(roleName);
        return this.findUsersByRoleId(role.getId(), Constants.EntityStatus.ACTIVE);
    }
}