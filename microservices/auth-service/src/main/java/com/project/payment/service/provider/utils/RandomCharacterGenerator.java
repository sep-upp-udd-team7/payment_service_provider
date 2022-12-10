package com.project.payment.service.provider.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomCharacterGenerator {

    public static String generateURLSafeString(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
}
