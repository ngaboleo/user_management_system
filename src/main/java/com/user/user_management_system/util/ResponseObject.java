package com.user.user_management_system.util;

import com.user.user_management_system.Message.IMessageService;
import lombok.Data;

@Data
public class ResponseObject {
    private Boolean status;
    private Object result;
    private String message;

    private static IMessageService messageService;

    public ResponseObject( Object object) {
        this.status = true;
        this.result = object;
        this.message = messageService.translate(IMessageService.SUCCESS);
    }

    public ResponseObject(Exception exception) {
        this.status = false;
        this.result = exception.getStackTrace();
        this.message = messageService.translate(exception.getMessage());
    }


}
