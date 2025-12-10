package com.apex.template.service;

import com.apex.template.common.util.DateTimeUtils;
import com.apex.template.common.util.PasswordUtil;
import com.apex.template.domain.Role;
import com.apex.template.domain.User;
import com.apex.template.repository.RoleRepository;
import com.apex.template.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Load User Roles
        // Load User Roles
        if (roleRepository.findById(1) == null)
            roleRepository.save(new Role(1,"ROLE_SUPER_ADMIN"));
        else if (!roleRepository.findById(1).getName().equals("ROLE_SUPER_ADMIN")) {
            Role oldAdminDate = roleRepository.findById(1);
            Role role = new Role();
            role.setId(oldAdminDate.getId());
            role.setName("ROLE_SUPER_ADMIN");
            roleRepository.save(role);
        }

        if (roleRepository.findById(2) == null)
            roleRepository.save(new Role(2,"ROLE_ADMIN"));
        else if (!roleRepository.findById(2).getName().equals("ROLE_ADMIN")) {
            Role oldAdminDate = roleRepository.findById(2);
            Role role = new Role();
            role.setId(oldAdminDate.getId());
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);
        }

        if (roleRepository.findById(3) == null)
            roleRepository.save(new Role(3,"ROLE_USER"));
        else if (!roleRepository.findById(3).getName().equals("ROLE_USER")) {
            Role oldAdminDate = roleRepository.findById(3);
            Role role = new Role();
            role.setId(oldAdminDate.getId());
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }

        if (roleRepository.findById(4) == null)
            roleRepository.save(new Role(4,"ROLE_MANAGER"));
        else if (!roleRepository.findById(4).getName().equals("ROLE_MANAGER")) {
            Role oldAdminDate = roleRepository.findById(3);
            Role role = new Role();
            role.setId(oldAdminDate.getId());
            role.setName("ROLE_MANAGER");
            roleRepository.save(role);
        }



        if (userRepository.findByUsername("apex@gmail.com") == null) {
            User user = new User();
            user.setName("apex");
            user.setPhone("0123456789");
            user.setEmail("apex@gmail.com");
            user.setCreatedOn(DateTimeUtils.dateWithTZ());
            user.setUsername("apex@gmail.com");
            user.setPassword(PasswordUtil.encryptPassword("00000000", PasswordUtil.EncType.BCRYPT_ENCODER, null));

            user.setRole(roleRepository.findById(1));

            userRepository.save(user);
        }

        if (userRepository.findByUsername("apex-manager@gmail.com") == null) {
            User user = new User();
            user.setName("apex-manager");
            user.setPhone("0123456789");
            user.setEmail("apex-manager@gmail.com");
            user.setCreatedOn(DateTimeUtils.dateWithTZ());
            user.setUsername("apex-manager@gmail.com");
            user.setPassword(PasswordUtil.encryptPassword("00000000", PasswordUtil.EncType.BCRYPT_ENCODER, null));

            user.setRole(roleRepository.findById(4));

            userRepository.save(user);
        }
    }
}