package com.example.auth.service;

/** Verifies a browser-provided Cloudflare Turnstile token before password checking. */
public interface TurnstileVerifier {
    void verify(String token, String clientIp);
}
