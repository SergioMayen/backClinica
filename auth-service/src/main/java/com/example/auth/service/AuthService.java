package com.example.auth.service;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.TokenResponse;
import com.example.auth.model.AppUser;
import com.example.auth.model.Role;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Clock;
import java.time.Duration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private final JwtEncoder jwtEncoder;
    private final long expiration;

    public AuthService(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder,
            Clock clock, JwtEncoder jwtEncoder,
            @Value("${security.jwt.expiration}") long expiration) {
        this.users = users; this.roles = roles; this.passwordEncoder = passwordEncoder;
        this.clock = clock; this.jwtEncoder = jwtEncoder; this.expiration = expiration;
    }

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (users.existsByUsername(request.username())) throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        Role role = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role("USER")));
        users.save(new AppUser(request.username(), passwordEncoder.encode(request.password()), role));
        return issue(request.username(), java.util.Set.of(role));
    }

    @Transactional(noRollbackFor = ResponseStatusException.class)
    public TokenResponse login(LoginRequest request) {
        AppUser user = users.findByUsernameForUpdate(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
        Instant now = clock.instant();
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        if (user.getLockedUntil() != null) user.resetFailedLogins();
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            user.recordFailedLogin();
            if (user.getFailedLoginAttempts() >= 5) user.lockUntil(now.plus(Duration.ofMinutes(15)));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        user.resetFailedLogins();
        return issue(user.getUsername(), user.getRoles());
    }

    private TokenResponse issue(String username, java.util.Set<Role> userRoles) {
        Instant now = clock.instant();
        String scope = userRoles.stream().map(Role::getName).collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("clinic-auth").issuedAt(now).expiresAt(now.plusSeconds(expiration))
                .subject(username).claim("scope", scope).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                org.springframework.security.oauth2.jwt.JwsHeader.with(SignatureAlgorithm.RS256).build(), claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", expiration);
    }
}
