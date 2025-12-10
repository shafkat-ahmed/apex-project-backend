package com.apex.template.service.implementation;

import com.apex.template.domain.User;
import com.apex.template.repository.UserRepository;
import com.apex.template.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

    private final UserService userService;

    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            User user = userService.getCurrentLoggedUser();
            return Optional.ofNullable(user.getId());
        } catch (Exception e) {
           return Optional.empty();
        }
    }
}

