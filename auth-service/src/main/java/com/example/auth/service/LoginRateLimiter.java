package com.example.auth.service;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/** Per-client request limit kept separate from the per-account password failure counter. */
@Component
public class LoginRateLimiter {
    private static final int LIMIT = 10;
    private static final long WINDOW_MILLIS = Duration.ofMinutes(1).toMillis();
    private final Clock clock;
    private final Map<String, ArrayDeque<Long>> requests = new HashMap<>();

    public LoginRateLimiter(Clock clock) { this.clock = clock; }

    public synchronized void check(String client) {
        long now = clock.millis();
        ArrayDeque<Long> times = requests.computeIfAbsent(client, ignored -> new ArrayDeque<>());
        while (!times.isEmpty() && now - times.peekFirst() >= WINDOW_MILLIS) times.removeFirst();
        if (times.size() >= LIMIT) throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Intenta de nuevo más tarde");
        times.addLast(now);
    }
}
