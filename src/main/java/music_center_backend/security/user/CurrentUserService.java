package music_center_backend.security.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getPublicId() { 
        return getAuthentication().getName();
    }

    public boolean hasRole(String role) {
        return getAuthentication().getAuthorities()
                .stream()
                .anyMatch(authority -> 
                        authority.getAuthority().equals("ROLE_" + role));
    }
}
