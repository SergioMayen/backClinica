package com.example.auth.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CloudflareTurnstileVerifier implements TurnstileVerifier {
    private static final URI SITEVERIFY = URI.create("https://challenges.cloudflare.com/turnstile/v0/siteverify");
    private static final Set<String> EXPECTED_ACTIONS = Set.of("login");
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private final ObjectMapper json;
    private final String secret;
    private final String expectedHostname;

    public CloudflareTurnstileVerifier(ObjectMapper json,
            @Value("${security.turnstile.secret:}") String secret,
            @Value("${security.turnstile.expected-hostname:backclinica-web.onrender.com}") String expectedHostname) {
        this.json = json;
        this.secret = secret == null ? "" : secret.trim();
        this.expectedHostname = expectedHostname == null ? "" : expectedHostname.trim();
    }

    @Override
    public void verify(String token, String clientIp) {
        if (secret.isBlank() || expectedHostname.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Verificación de seguridad no configurada");
        }
        if (token == null || token.isBlank()) reject();
        String body = "secret=" + encode(secret) + "&response=" + encode(token.trim());
        if (clientIp != null && !clientIp.isBlank()) body += "&remoteip=" + encode(clientIp);
        HttpRequest request = HttpRequest.newBuilder(SITEVERIFY)
                .timeout(Duration.ofSeconds(8))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) unavailable();
            validateSiteverifyResponse(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            unavailable();
        } catch (IOException e) {
            unavailable();
        }
    }

    private static String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8); }
    void validateSiteverifyResponse(String responseBody) throws IOException {
        SiteverifyResult result = json.readValue(responseBody, SiteverifyResult.class);
        if (result == null || !result.success() || !expectedHostname.equals(result.hostname()) || !EXPECTED_ACTIONS.contains(result.action())) reject();
    }
    private static void reject() { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"); }
    private static void unavailable() { throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No fue posible validar la solicitud"); }

    private record SiteverifyResult(boolean success, String hostname, String action,
            @JsonProperty("error-codes") java.util.List<String> errorCodes) {}
}
