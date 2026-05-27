package music_center_backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserProvider {
    public String getCurrentUserPublicId() { 
        Authentication authentication = 
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
}
