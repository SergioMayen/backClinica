package com.example.auth.service;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        check(client, null);
    }

    public synchronized void check(String client, String username) {
        long now = clock.millis();
        List<String> keys = new ArrayList<>();
        if (client != null && !client.isBlank()) keys.add("client:" + client);
        if (username != null && !username.isBlank()) keys.add("account:" + username.trim().toLowerCase(Locale.ROOT));
        for (String key : keys) {
            ArrayDeque<Long> times = requests.computeIfAbsent(key, ignored -> new ArrayDeque<>());
            while (!times.isEmpty() && now - times.peekFirst() >= WINDOW_MILLIS) times.removeFirst();
            if (times.size() >= LIMIT) throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Intenta de nuevo más tarde");
        }
        for (String key : keys) requests.get(key).addLast(now);
    }
}
