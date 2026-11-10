package com.example.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class CloudflareTurnstileVerifierTest {
    private CloudflareTurnstileVerifier verifier;

    @BeforeEach void setUp() {
        verifier = new CloudflareTurnstileVerifier(new ObjectMapper(), "fake-test-secret", "backclinica-web.onrender.com");
    }

    @Test void acceptsOnlySuccessfulResponseForExpectedHostAndLoginAction() throws IOException {
        assertDoesNotThrow(() -> verifier.validateSiteverifyResponse("{\"success\":true,\"hostname\":\"backclinica-web.onrender.com\",\"action\":\"login\"}"));
    }

    @Test void rejectsFailedVerificationWrongHostAndWrongAction() {
        assertRejected("{\"success\":false,\"hostname\":\"backclinica-web.onrender.com\",\"action\":\"login\"}");
        assertRejected("{\"success\":true,\"hostname\":\"attacker.example\",\"action\":\"login\"}");
        assertRejected("{\"success\":true,\"hostname\":\"backclinica-web.onrender.com\",\"action\":\"register\"}");
    }

    @Test void failsClosedWhenServerSecretIsMissing() {
        CloudflareTurnstileVerifier unconfigured = new CloudflareTurnstileVerifier(new ObjectMapper(), "", "backclinica-web.onrender.com");
        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> unconfigured.verify("token", "203.0.113.10"));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
    }

    private void assertRejected(String responseBody) {
        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> verifier.validateSiteverifyResponse(responseBody));
        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
        assertEquals("Credenciales inválidas", error.getReason());
    }
}
