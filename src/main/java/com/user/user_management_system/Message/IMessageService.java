package com.user.user_management_system.Message;

public interface IMessageService {
    String SUCCESS = "Request processed successful";
    String USER_ACCOUNT_DISABLED = "user.account.disabled";
    String BAD_CREDENTIALS = "user.credentials.failed";


    String translate(String message);
}
