package com.rand.ishowApi.audit;

import com.rand.ishowApi.security.context.UserContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class KeycloakAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return Optional.empty();
        }

        if (UserContext.getUserId() != null && !UserContext.getUserId().isEmpty()) {
            return Optional.of(UserContext.getUserId());
        }
        return Optional.of(auth.getName());
    }
}