package com.sampledashboard1.utils;

import com.sampledashboard1.config.security.CustomUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
    public static String generateRandomStringOnlyAlphabet(int length) {
        final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
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
    public static CustomUser getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (CustomUser) authentication.getPrincipal();
    }
    public static String getCurrentLoginId() {
        CustomUser userPrincipal = getCurrentAuditor();
        return userPrincipal == null ? "" : userPrincipal.getId().toString();
    }
    public static String getCurrentUserId() {
        CustomUser userPrincipal = getCurrentAuditor();
        return userPrincipal == null ? "" : userPrincipal.getCurrentUserId();
    }
}
