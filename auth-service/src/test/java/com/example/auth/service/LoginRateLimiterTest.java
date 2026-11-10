package com.example.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class LoginRateLimiterTest {
    @Test void eleventhRequestInOneMinuteGets429AndWindowResets() {
        MutableClock clock = new MutableClock();
        LoginRateLimiter limiter = new LoginRateLimiter(clock);
        for (int i = 0; i < 10; i++) limiter.check("demo-client");
        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> limiter.check("demo-client"));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, error.getStatusCode());
        clock.advance(Duration.ofMinutes(1));
        assertDoesNotThrow(() -> limiter.check("demo-client"));
    }

    private static final class MutableClock extends Clock {
        private Instant instant = Instant.parse("2026-01-01T00:00:00Z");
        void advance(Duration duration) { instant = instant.plus(duration); }
        @Override public ZoneId getZone() { return ZoneId.of("UTC"); }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return instant; }
    }
}
