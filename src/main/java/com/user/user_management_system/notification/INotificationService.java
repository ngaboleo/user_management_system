package com.user.user_management_system.notification;

public interface INotificationService {

    void sendEmail(final String recipientId, final String subject, final String body);

    void sendSms(String message, String recipient);
}
