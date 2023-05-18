package com.user.user_management_system.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService{

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Override
    public void sendEmail(String recipientId, String subject, String body) {
        try {
            Thread thread = new Thread(() -> {
               this.sendEmail(recipientId, subject, body);
            });
            thread.setDaemon(true);
            thread.start();
        }catch (Exception ex){
            logger.error("Unable to send email to : ", recipientId);
        }
    }

    // to do
    @Override
    public void sendSms(String message, String recipient) {

    }
}
