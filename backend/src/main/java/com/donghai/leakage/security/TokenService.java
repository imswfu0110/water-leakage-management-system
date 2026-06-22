package com.donghai.leakage.security;

import com.donghai.leakage.common.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class TokenService {
    private final byte[] secret;
    private final long expireSeconds;

    public TokenService(@Value("${app.token-secret}") String secret,
                        @Value("${app.token-expire-hours:12}") long expireHours) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expireSeconds = expireHours * 3600;
    }

    public String create(Long userId) {
        String payload = userId + ":" + (Instant.now().getEpochSecond() + expireSeconds);
        return encode(payload) + "." + sign(payload);
    }

    public Long parse(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 2) throw new IllegalArgumentException();
            String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            if (!constantTimeEquals(sign(payload), parts[1])) throw new IllegalArgumentException();
            String[] values = payload.split(":");
            if (Long.parseLong(values[1]) < Instant.now().getEpochSecond()) {
                throw new BusinessException(401, "登录状态已过期，请重新登录");
            }
            return Long.parseLong(values[0]);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(401, "无效的登录凭证");
        }
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Token signing failed", e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        return java.security.MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }
}
