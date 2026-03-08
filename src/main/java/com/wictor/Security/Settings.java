package com.wictor.Security;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Settings {
    public static final String pepper = System.getenv("APP_PEPPER");

    public static SecretKeySpec getKey() throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] key = digest.digest(pepper.getBytes(StandardCharsets.UTF_8));

        return new SecretKeySpec(key, "AES");
    }
}
