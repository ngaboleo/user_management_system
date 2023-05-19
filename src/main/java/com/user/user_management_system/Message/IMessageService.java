package com.user.user_management_system.Message;

public interface IMessageService {
    String SUCCESS = "Request processed successful";
    String USER_ACCOUNT_DISABLED = "user.account.disabled";
    String BAD_CREDENTIALS = "user.credentials.failed";
    String USERNAME_NOT_FOUND = "username.notFound";
    String USER_NOT_ALLOWED = "user.notAllowed";
    String USER_NOT_FOUND = "user.notFound";
    String ROLE_NOT_FOUND = "role.notFound";
    String PERMISSION_NOT_FOUND= "permission.notFound";

    String translate(String message);
}
