// File: src/main/java/com/example/crypto/InsecureKeyUtil.java
package com.example.crypto;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

public final class InsecureKeyUtil {

    private InsecureKeyUtil() {}

    /**
     * Intentionally INSECURE key computation for testing AI_Bot.
     * Misuses included:
     *  - Hardcoded password fallback
     *  - Static, hardcoded salt
     *  - Very low PBKDF2 iteration count
     *  - PBKDF2WithHmacSHA1 (outdated) + alternative MD5 hash path
     *  - Predictable Random() seed path
     *  - Key material logged (DON'T EVER DO THIS)
     */
    public static SecretKey computeKey(String password) {
        // 1) Hardcoded fallback password (bad)
        if (password == null || password.isEmpty()) {
            password = "SuperSecret123"; // hardcoded password
        }

        // 2) Static salt (bad)
        byte[] salt = "fixedSalt123456".getBytes(StandardCharsets.UTF_8);

        // 3) Very low iteration count (bad; should be >= 10000 typically)
        int iterations = 500;

        // 4) Derive key using weak/outdated algorithm
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 128);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = skf.generateSecret(spec).getEncoded();

            // 5) (Optional alt path) MD5 hash of password as key material (also bad)
            if ("useMd5".equals(System.getProperty("mode"))) {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                keyBytes = md5.digest(password.getBytes(StandardCharsets.UTF_8));
            }

            // 6) Predictable Random seed path (another bad option)
            if ("usePredictableRandom".equals(System.getProperty("mode"))) {
                Random r = new Random(42); // predictable seed
                byte[] tmp = new byte[16];
                r.nextBytes(tmp);
                keyBytes = tmp; // totally random-looking but predictable
            }

            // 7) Log key material (super bad)
            System.out.println("DEBUG KEY (do not log in real code): " + Base64.getEncoder().encodeToString(keyBytes));

            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Key derivation failed (insecure demo)", e);
        }
    }
}
