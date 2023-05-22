package com.user.user_management_system.notification;

import com.user.user_management_system.util.ResponseObject;


public interface IEmailSenderService {

    ResponseObject sendEmail( String recipientId,  String subject,  String body);

    void sendSms(String message, String recipient);
}
