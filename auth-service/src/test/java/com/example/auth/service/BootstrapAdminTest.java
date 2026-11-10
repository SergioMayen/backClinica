package com.example.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.auth.model.AppUser;
import com.example.auth.model.Role;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class BootstrapAdminTest {
    private final UserRepository users = mock(UserRepository.class);
    private final RoleRepository roles = mock(RoleRepository.class);
    private final PasswordEncoder passwords = mock(PasswordEncoder.class);

    @Test void createsAdminWhenConfigured() {
        when(roles.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());
        when(roles.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(users.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwords.encode("long-test-password")).thenReturn("encoded");

        new BootstrapAdmin(users, roles, passwords, "admin", "long-test-password").run();

        verify(users).save(argThat(user -> user.getUsername().equals("admin")
                && user.getPassword().equals("encoded")
                && user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))));
    }

    @Test void updatesExistingAdminPasswordWithoutClearingLockState() {
        Role userRole = new Role("USER");
        AppUser existing = new AppUser("admin", "old-hash", userRole);
        existing.recordFailedLogin();
        when(roles.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());
        when(roles.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(users.findByUsername("admin")).thenReturn(Optional.of(existing));
        when(passwords.matches("long-test-password", "old-hash")).thenReturn(false);
        when(passwords.encode("long-test-password")).thenReturn("new-hash");

        new BootstrapAdmin(users, roles, passwords, "admin", "long-test-password").run();

        assertEquals("new-hash", existing.getPassword());
        assertEquals(1, existing.getFailedLoginAttempts());
        assertEquals(2, existing.getRoles().size());
        verify(users).save(existing);
    }

    @Test void leavesExistingPasswordAloneWhenItAlreadyMatches() {
        AppUser existing = new AppUser("admin", "current-hash", new Role("ADMIN"));
        when(roles.findByName("ROLE_ADMIN")).thenReturn(Optional.of(existing.getRoles().iterator().next()));
        when(users.findByUsername("admin")).thenReturn(Optional.of(existing));
        when(passwords.matches("long-test-password", "current-hash")).thenReturn(true);

        new BootstrapAdmin(users, roles, passwords, "admin", "long-test-password").run();

        assertEquals("current-hash", existing.getPassword());
        verify(passwords, never()).encode(any());
        verify(users).save(existing);
    }
}
