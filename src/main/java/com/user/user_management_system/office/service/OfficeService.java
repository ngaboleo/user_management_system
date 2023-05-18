package com.user.user_management_system.office.service;

import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.office.dto.OfficeDto;
import com.user.user_management_system.office.model.IOfficeRepository;
import com.user.user_management_system.office.model.Office;
import com.user.user_management_system.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class OfficeService implements IOfficeService{
    @Autowired
    private IOfficeRepository iOfficeRepository;



    @Override
    public ResponseObject createOffice(OfficeDto officeDto) {
        try {
            Office office = new Office();
            BeanUtils.copyProperties(officeDto, office);
            office.setIsActive(true);
//            office.setOfficeName(officeDto.getOfficeName());
//            office.setOfficeLocation(officeDto.getOfficeLocation());
//            Office officeSaved = iOfficeRepository.save(office);
            return new ResponseObject(iOfficeRepository.save(office));
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject updateOffice(UUID officeId, OfficeDto officeDto) {
        return null;
    }

    @Override
    public ResponseObject getAllOffice(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
