package com.care.hub.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final byte[] secret;
    private final long expirationSeconds;

    public JwtUtil(
            @Value("${jwt.secret:change-me-please-very-strong-secret-key-32-bytes-min}") String secret,
            @Value("${jwt.expiration-seconds:86400}") long expirationSeconds
    ) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(UserDetails userDetails) {
        try {
            var header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            var now = Instant.now();
            var exp = now.plusSeconds(expirationSeconds);

            var roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            var payload = new HashMap<>();
            payload.put("sub", userDetails.getUsername());
            payload.put("roles", roles);
            payload.put("iat", now.getEpochSecond());
            payload.put("exp", exp.getEpochSecond());

            var headerB64 = base64UrlEncode(objectMapper.writeValueAsBytes(header));
            var payloadB64 = base64UrlEncode(objectMapper.writeValueAsBytes(payload));

            var unsignedToken = headerB64 + "." + payloadB64;
            var signatureB64 = base64UrlEncode(sign(unsignedToken.getBytes(StandardCharsets.UTF_8)));

            return unsignedToken + "." + signatureB64;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            var parts = token.split("\\.");
            if (parts.length != 3) return false;

            var unsigned = parts[0] + "." + parts[1];
            var expectedSig = sign(unsigned.getBytes(StandardCharsets.UTF_8));
            var providedSig = base64UrlDecode(parts[2]);

            if (!MessageDigest.isEqual(expectedSig, providedSig)) {
                return false;
            }

            var payload = parsePayload(parts[1]);
            var username = (String) payload.get("sub");
            var exp = (Number) payload.get("exp");

            if (username == null || exp == null) return false;

            var nowSec = Instant.now().getEpochSecond();
            if (nowSec >= exp.longValue()) return false;

            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        var parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Token inválido");
        }
        var payload = parsePayload(parts[1]);
        var sub = payload.get("sub");
        if (sub == null) {
            throw new IllegalArgumentException("Token sem subject");
        }
        return sub.toString();
    }

    private Map<String, Object> parsePayload(String payloadB64) {
        try {
            var payloadBytes = base64UrlDecode(payloadB64);
            return objectMapper.readValue(payloadBytes, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao analisar payload do token", e);
        }
    }

    private byte[] sign(byte[] data) throws Exception {
        var mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret, "HmacSHA256"));
        return mac.doFinal(data);
    }

    private static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static byte[] base64UrlDecode(String data) {
        return Base64.getUrlDecoder().decode(data);
    }
}
