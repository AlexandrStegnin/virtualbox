package ru.stegnin.virtualbox.settings.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.stegnin.virtualbox.api.repository.UserRepository;

@Service
public class SecurityUserHelper {

    private final UserRepository userRepo;

    @Autowired
    public SecurityUserHelper(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public String getUserSyncFolder() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser) {
            return ((SecurityUser) principal).getSettings().getSyncFolder();
        } else {
            return userRepo.findByLogin(principal.toString()).getSettings().getSyncFolder();
        }
    }
}
