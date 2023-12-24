package vn.winter.usercenter.util;

import java.util.Random;

public class Util {
    public static String generateRandomSaltCode(int length) {
        int leftLimit = 48;     // 33
        int rightLimit = 122;   // 126
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i > 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String trim(String str) {
        if (str != null) {
            return str.trim();
        } else {
            return null;
        }
    }
}
