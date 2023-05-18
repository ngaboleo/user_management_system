package com.user.user_management_system.notification;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface INotificationService {

    void sendEmail(final String recipientId, final String subject, final String body);

    void sendSms(String message, String recipient);
}
