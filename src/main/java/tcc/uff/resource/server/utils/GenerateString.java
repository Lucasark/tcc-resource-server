package tcc.uff.resource.server.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateString {

    private static final String CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static Random rand;

    static {
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error no SecureRandom");
        }
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(CHAR.length());
            char randomChar = CHAR.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString().toUpperCase();
    }
}
