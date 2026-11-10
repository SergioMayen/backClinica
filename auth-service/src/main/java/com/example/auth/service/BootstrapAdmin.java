package com.example.auth.service;

import com.example.auth.model.AppUser;
import com.example.auth.model.Role;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Creates the initial administrator only when explicit Render secrets are configured. */
@Component
public class BootstrapAdmin implements CommandLineRunner {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public BootstrapAdmin(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder,
            @Value("${BOOTSTRAP_ADMIN_USERNAME:}") String username,
            @Value("${BOOTSTRAP_ADMIN_PASSWORD:}") String password) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (username.isBlank() && password.isBlank()) return;
        if (username.isBlank() || password.isBlank() || password.length() < 12) {
            throw new IllegalStateException("Set both bootstrap admin secrets and use a password of at least 12 characters.");
        }
        Role adminRole = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ADMIN")));
        Optional<AppUser> existing = users.findByUsername(username);
        if (existing.isPresent()) {
            AppUser admin = existing.get();
            admin.getRoles().add(adminRole);
            if (!passwordEncoder.matches(password, admin.getPassword())) {
                admin.updatePassword(passwordEncoder.encode(password));
            }
            users.save(admin);
            return;
        }
        users.save(new AppUser(username, passwordEncoder.encode(password), adminRole));
    }
}
