package com.user.user_management_system.notification;

import com.user.user_management_system.Message.IMessageService;
import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.util.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService implements IEmailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public ResponseObject sendEmail(String recipientId, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(recipientId);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            javaMailSender.send(mailMessage);
            return new ResponseObject(IMessageService.EMAIL_SEND);
        }catch (Exception ex){
            logger.error("Unable to send email to : ", recipientId);
            throw new HandleException(ex);
        }
    }

    // to do
    @Override
    public void sendSms(String message, String recipient) {

    }
}
