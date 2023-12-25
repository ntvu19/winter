package vn.winter.usercenter.util;

import java.util.Random;

public class Util {
    public static String generateRandomSalt(int length) {
        int leftLimit = 48;     // 33
        int rightLimit = 122;   // 126
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i > 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateRandomOtp(int length) {
        int leftLimit = 48;
        int rightLimit = 57;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }
}
