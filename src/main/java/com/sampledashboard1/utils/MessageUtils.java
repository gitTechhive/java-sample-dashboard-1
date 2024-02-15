package com.sampledashboard1.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Log4j2
@Component
public class MessageUtils {
    private static MessageSource messageSource;

    private MessageUtils() {
    }

    @Autowired
    private MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    public static String get(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    public static String get(String code, Object[] args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }
}
