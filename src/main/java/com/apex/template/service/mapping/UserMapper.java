package com.apex.template.service.mapping;

import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.common.util.PasswordUtil;
import com.apex.template.common.util.RepositoryUtilityMethod;
import com.apex.template.domain.Role;
import com.apex.template.domain.User;
import com.apex.template.domain.dto.UserDTO;
import com.apex.template.repository.RoleRepository;
import com.apex.template.repository.UserRepository;
import com.apex.template.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

import static com.apex.template.common.util.PasswordUtil.EncType.BCRYPT_ENCODER;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserMapper {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Function<UserDTO, User> requestMapper = this::entity;
    public Function<User, UserDTO> responseMapper = this::dto;

    private User entity(UserDTO dto) throws EntityNotFoundException {
        User entity = new User();

        if (dto.getId() != null) {
            User existingUser = RepositoryUtilityMethod.getObject(dto.getId(), userRepository);
            entity.setId(existingUser.getId());
            entity.setCreatedOn(existingUser.getCreatedOn());
            entity.setPassword(existingUser.getPassword());
        }
        else{
            entity.setCreatedOn(new Date());

            try {
                entity.setPassword(PasswordUtil.encryptPassword(dto.getPassword(),PasswordUtil.EncType.BCRYPT_ENCODER, null));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setPhone(dto.getPhone());

        // Role is mandatory
        entity.setRole(roleRepository.findById(dto.getRoleId()).orElseThrow(()->new EntityNotFoundException("no object found with id: "+dto.getRoleId())));


        return entity;
    }

    private UserDTO dto(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUsername(entity.getUsername());
        dto.setPhone(entity.getPhone());

        if (entity.getRole() != null) {
            dto.setRoleId(entity.getRole().getId());
        }


        return dto;
    }
}