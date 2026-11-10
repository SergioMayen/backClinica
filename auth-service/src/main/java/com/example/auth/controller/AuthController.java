package com.example.auth.controller;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.TokenResponse;
import com.example.auth.service.AuthService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.auth.service.LoginRateLimiter;
import com.example.auth.service.TurnstileVerifier;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RSAPublicKey publicKey;
    private final LoginRateLimiter loginRateLimiter;
    private final TurnstileVerifier turnstileVerifier;
    public AuthController(AuthService authService, RSAPublicKey publicKey, LoginRateLimiter loginRateLimiter, TurnstileVerifier turnstileVerifier) { this.authService = authService; this.publicKey = publicKey; this.loginRateLimiter = loginRateLimiter; this.turnstileVerifier = turnstileVerifier; }

    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@Valid @RequestBody RegisterRequest request) { return authService.register(request); }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        loginRateLimiter.check(servletRequest.getRemoteAddr(), request.username());
        turnstileVerifier.verify(request.turnstileToken(), servletRequest.getRemoteAddr());
        return authService.login(request);
    }

    @GetMapping("/healthz")
    public Map<String, String> health() { return Map.of("status", "ok"); }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) { return Map.of("username", authentication.getName(), "authorities", authentication.getAuthorities()); }

    @GetMapping("/jwks.json")
    public Map<String, Object> jwks() {
        RSAKey key = new RSAKey.Builder(publicKey).keyID("clinic-auth-rsa").build();
        return new JWKSet(key).toJSONObject();
    }
}
