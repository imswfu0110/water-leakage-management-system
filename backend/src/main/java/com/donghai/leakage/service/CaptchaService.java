package com.donghai.leakage.service;

import com.donghai.leakage.common.BusinessException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CaptchaService {
    private record Entry(String answer, long expiresAt) {}
    private final Map<String, Entry> cache = new ConcurrentHashMap<>();

    public Map<String, String> create() {
        cleanup();
        int a = ThreadLocalRandom.current().nextInt(1, 10);
        int b = ThreadLocalRandom.current().nextInt(1, 10);
        String id = UUID.randomUUID().toString();
        cache.put(id, new Entry(String.valueOf(a + b), Instant.now().getEpochSecond() + 180));
        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='118' height='42'>"
                + "<rect width='118' height='42' rx='6' fill='#e8f4ff'/><path d='M4 30L110 8M8 8L113 34' stroke='#9ac8ef' opacity='.45'/>"
                + "<text x='59' y='28' text-anchor='middle' font-size='22' font-weight='700' fill='#1976c9' font-family='Arial'>"
                + a + " + " + b + " = ?</text></svg>";
        return Map.of("captchaId", id, "image", "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8)));
    }

    public void verify(String id, String answer) {
        Entry entry = cache.remove(id);
        if (entry == null || entry.expiresAt < Instant.now().getEpochSecond() || !entry.answer.equals(answer.trim())) {
            throw new BusinessException("验证码不正确或已过期");
        }
    }

    private void cleanup() {
        long now = Instant.now().getEpochSecond();
        cache.entrySet().removeIf(e -> e.getValue().expiresAt < now);
    }
}
