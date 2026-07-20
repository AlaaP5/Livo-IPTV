package com.rand.ishowApi.messages.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageResolver {
    private static MessageSource messageSource;
    public MessageResolver(MessageSource messageSource) {
        MessageResolver.messageSource = messageSource;
    }
    public static String resolve(String code, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, code, locale);
    }
}
