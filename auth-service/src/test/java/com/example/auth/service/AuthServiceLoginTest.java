package com.example.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.auth.dto.LoginRequest;
import com.example.auth.model.AppUser;
import com.example.auth.model.Role;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.server.ResponseStatusException;

class AuthServiceLoginTest {
    private final MutableClock clock = new MutableClock(Instant.parse("2026-01-01T00:00:00Z"));
    private final UserRepository users = mock(UserRepository.class);
    private final RoleRepository roles = mock(RoleRepository.class);
    private final PasswordEncoder passwords = mock(PasswordEncoder.class);
    private final JwtEncoder jwtEncoder = mock(JwtEncoder.class);
    private final AppUser account = new AppUser("demo", "hashed", new Role("USER"));
    private AuthService service;

    @BeforeEach void setUp() {
        service = new AuthService(users, roles, passwords, clock, jwtEncoder, 3600);
        when(users.findByUsernameForUpdate("demo")).thenReturn(Optional.of(account));
    }

    @Test void fifthBadPasswordLocksForFifteenMinutesAndCorrectPasswordGetsNoToken() {
        when(passwords.matches("bad", "hashed")).thenReturn(false);
        for (int i = 0; i < 5; i++) assertStatus(HttpStatus.UNAUTHORIZED, () -> service.login(new LoginRequest("demo", "bad", "valid-test-token")));
        assertEquals(5, account.getFailedLoginAttempts());
        assertEquals(clock.instant().plus(Duration.ofMinutes(15)), account.getLockedUntil());

        when(passwords.matches("correct", "hashed")).thenReturn(true);
        assertStatus(HttpStatus.UNAUTHORIZED, () -> service.login(new LoginRequest("demo", "correct", "valid-test-token")));
        verifyNoInteractions(jwtEncoder);
        assertEquals(clock.instant().plus(Duration.ofMinutes(15)), account.getLockedUntil());
    }

    @Test void lockExpiresAndSuccessfulLoginResetsCounter() {
        account.lockUntil(clock.instant().plus(Duration.ofMinutes(15)));
        account.recordFailedLogin();
        when(passwords.matches("correct", "hashed")).thenReturn(true);
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("test-token");
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        assertStatus(HttpStatus.UNAUTHORIZED, () -> service.login(new LoginRequest("demo", "correct", "valid-test-token")));
        clock.advance(Duration.ofMinutes(15));
        assertEquals("test-token", service.login(new LoginRequest("demo", "correct", "valid-test-token")).accessToken());
        assertEquals(0, account.getFailedLoginAttempts());
        assertNull(account.getLockedUntil());
    }

    private static void assertStatus(HttpStatus status, Runnable action) {
        ResponseStatusException error = assertThrows(ResponseStatusException.class, action::run);
        assertEquals(status, error.getStatusCode());
        assertEquals("Credenciales inválidas", error.getReason());
    }

    private static final class MutableClock extends Clock {
        private Instant instant;
        MutableClock(Instant instant) { this.instant = instant; }
        void advance(Duration duration) { instant = instant.plus(duration); }
        @Override public ZoneId getZone() { return ZoneId.of("UTC"); }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return instant; }
    }
}
