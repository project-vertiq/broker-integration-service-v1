package com.vertiq.broker.integration.service.v1.utils;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAccessTokenUtil {

    public static Long getExpiryEpochSeconds(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = mapper.readValue(payloadJson, Map.class);
            Object exp = payload.get("exp");
            if (exp instanceof Number) {
                return ((Number) exp).longValue();
            } else if (exp instanceof String) {
                return Long.parseLong((String) exp);
            }
        } catch (Exception e) {
            // log error or handle as needed
        }
        return null;
    }
}
