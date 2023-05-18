package com.user.user_management_system.office.controller;

import com.user.user_management_system.office.dto.OfficeDto;
import com.user.user_management_system.office.model.Office;
import com.user.user_management_system.office.service.OfficeService;
import com.user.user_management_system.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/office")
public class OfficeController {
    @Autowired
    private OfficeService officeService;

    @PostMapping("/create")
    public ResponseObject createOffice(@RequestBody OfficeDto officeDto){
        try {
            return officeService.createOffice(officeDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
}
