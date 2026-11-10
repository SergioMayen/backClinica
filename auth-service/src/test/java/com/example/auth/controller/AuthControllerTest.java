package com.example.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.auth.dto.LoginRequest;
import com.example.auth.service.AuthService;
import com.example.auth.service.LoginRateLimiter;
import com.example.auth.service.TurnstileVerifier;
import java.security.interfaces.RSAPublicKey;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

class AuthControllerTest {
    @Test void invalidTurnstileIsRejectedBeforePasswordCheck() {
        AuthService authService = mock(AuthService.class);
        LoginRateLimiter limiter = mock(LoginRateLimiter.class);
        TurnstileVerifier turnstile = mock(TurnstileVerifier.class);
        AuthController controller = new AuthController(authService, mock(RSAPublicKey.class), limiter, turnstile);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("203.0.113.7");
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"))
                .when(turnstile).verify("invalid-token", "203.0.113.7");

        ResponseStatusException failure = assertThrows(ResponseStatusException.class,
                () -> controller.login(new LoginRequest("demo", "wrong-password", "invalid-token"), request));

        assertEquals(HttpStatus.UNAUTHORIZED, failure.getStatusCode());
        verify(limiter).check("203.0.113.7", "demo");
        verify(turnstile).verify("invalid-token", "203.0.113.7");
        verifyNoInteractions(authService);
    }
}
