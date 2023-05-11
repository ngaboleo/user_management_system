package com.user.user_management_system.Message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class MessageService implements IMessageService{
    private MessageSource messageSource;
    @Override
    public String translate(String message) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(message, null, locale);
        }catch (Exception exception){
            return message;
        }
    }
}
