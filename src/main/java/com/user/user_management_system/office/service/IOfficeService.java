package com.user.user_management_system.office.service;

import com.user.user_management_system.office.dto.OfficeDto;
import com.user.user_management_system.util.ResponseObject;

import java.util.UUID;

public interface IOfficeService {
    ResponseObject createOffice(OfficeDto officeDto);
    ResponseObject updateOffice(UUID officeId, OfficeDto officeDto);
    ResponseObject getAllOffice(Integer pageNumber, Integer pageSize);
}
