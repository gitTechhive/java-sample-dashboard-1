package com.sampledashboard1.utils;

import lombok.extern.log4j.Log4j2;

import java.security.SecureRandom;
import java.util.List;

@Log4j2
public class MethodUtils {
    private MethodUtils() {
    }

    public static <T> boolean isObjectisNullOrEmpty(T... t) {
        for (T ob : t) {
            if (ob == null || ob.toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    public static <T> boolean isListIsNullOrEmpty(List<T> listT) {
        return (listT == null || listT.isEmpty());
    }
    public static String generateRandomString(int length) {
          final String CHARACTERS = "!@#$%^&*()_+<>?abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public static String generateRandomInteger(int length) {
        final String CHARACTERS = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
