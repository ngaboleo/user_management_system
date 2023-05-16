package com.user.user_management_system.user.service;

import com.user.user_management_system.Message.IMessageService;
import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.office.model.IOfficeRepository;
import com.user.user_management_system.office.model.Office;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.user.auth.UserImplDetailService;
import com.user.user_management_system.user.dto.LoginRequest;
import com.user.user_management_system.user.dto.LoginResponseDto;
import com.user.user_management_system.user.dto.UpdatePasswordDto;
import com.user.user_management_system.user.dto.UserDto;
import com.user.user_management_system.user.model.IUserRepository;
import com.user.user_management_system.user.model.User;
import com.user.user_management_system.util.ResponseObject;
import com.user.user_management_system.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService{
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IOfficeRepository iOfficeRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserImplDetailService userImplDetailService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private Office office;
    private Set<Role> roles;
    @Override
    public ResponseObject createUser(UserDto userDto) {
        try {
            if (validateUserDto(userDto)){
                List<User> users = iUserRepository.findUsersByEmailOrPhoneNumber(userDto.getEmail(), userDto.getPhoneNumber());
                if (users.isEmpty()){
                    Optional<Office> optionalOffice = iOfficeRepository.findById(userDto.getOffice_id());
                    if (optionalOffice.isPresent()){
                        User currentUser = new User();
                        BeanUtils.copyProperties(userDto, currentUser);
                        currentUser.setOffice(office);
                        currentUser.setRoles(roles);
                        currentUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
                        User userSaved = iUserRepository.save(currentUser);

                        // to do message and send email

                        return new ResponseObject(userSaved);
                    }else {
                        throw new HandleException("invalid office id");
                    }
                }else {
                    throw new HandleException("email already exists");
                }
            }else {
                throw new HandleException("required user information is missing");
            }
        }catch (Exception ex){
            throw new HandleException(ex);
        }
    }

    private boolean validateUserDto(UserDto userDto) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return !ObjectUtils.isEmpty(userDto.getFullName()) && !ObjectUtils.isEmpty(userDto.getRole_id())
                && !ObjectUtils.isEmpty(userDto.getOffice_id()) && !ObjectUtils.isEmpty(userDto.getDocumentType())
                && !ObjectUtils.isEmpty(userDto.getDocumentId()) && !ObjectUtils.isEmpty(userDto.getJurisdictionLevel())
                && !emailValidator.isValid(userDto.getEmail())
                && !ObjectUtils.isEmpty(userDto.getPhoneNumber()) && userDto.getPhoneNumber().length() == 10;
    }

    @Override
    public ResponseObject login(LoginRequest loginRequest) {
        try {
            authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            final UserDetails userDetails = userImplDetailService.loadUserByUsername(loginRequest.getEmail());

            //extract user info
            Optional<User> optionalUser = iUserRepository.findUserByEmailIgnoreCase(loginRequest.getEmail());
            if (optionalUser.isPresent()){
                final String token = tokenUtil.generateToken(userDetails);
                User loggedUser = optionalUser.get();
                return new ResponseObject(new LoginResponseDto(token, loggedUser));
            }else {
                throw new HandleException("user not found");
            }
        }catch (Exception ex){
            throw new HandleException(ex);
        }
    }

    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (DisabledException e){
            throw new HandleException(IMessageService.USER_ACCOUNT_DISABLED);
        }catch (BadCredentialsException e){
            throw new HandleException(IMessageService.BAD_CREDENTIALS);
        }
    }

    @Override
    public ResponseObject updateUser(UUID userId, UserDto userDto) {
        try {
            Optional<User> optionalUser = iUserRepository.findUserById(userId);
            if (optionalUser.isPresent() && !ObjectUtils.isEmpty(userDto.getEmail())){
                User updateUser = setUpdatedUser(userDto, optionalUser.get());
                return new ResponseObject(iUserRepository.save(updateUser));
            }else {
                return new ResponseObject(IMessageService.USER_NOT_FOUND);
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }

    }

    private User setUpdatedUser(UserDto userDto, User user) {
        User currentUser = iUserRepository.findUserByEmailIgnoreCase(userDto.getEmail()).orElseThrow(() -> new HandleException(IMessageService.USERNAME_NOT_FOUND));
        if (currentUser.getId().equals(user.getId()) || currentUser.getRoles().equals(user.getRoles())){
            EmailValidator emailValidator = EmailValidator.getInstance();
            if (!ObjectUtils.isEmpty(userDto.getEmail()) && emailValidator.isValid(userDto.getEmail())){
                if (!ObjectUtils.isEmpty(currentUser.getEmail())){
                    if (!currentUser.getEmail().equals(userDto.getEmail())){
                        currentUser.setEmail(userDto.getEmail());
                    }
                }else {
                    currentUser.setEmail(userDto.getEmail());
                }
                currentUser.setEmail(userDto.getEmail());
            }
            if (!ObjectUtils.isEmpty(userDto.getFullName()) && !currentUser.getFullName().equals(userDto.getFullName())){
                currentUser.setFullName(userDto.getFullName());
            }
            return currentUser;
        }else {
            throw new HandleException(IMessageService.USER_NOT_ALLOWED);
        }
    }

    @Override
    public ResponseObject getAllUser(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public ResponseObject findUserById(UUID id) {
        return null;
    }

    @Override
    public ResponseObject findActiveUsersByRole(UUID roleId) {
        return null;
    }

    @Override
    public ResponseObject updatePassword(UpdatePasswordDto updatePasswordDto) {
        return null;
    }

    @Override
    public ResponseObject resetPassword(String email, String otp) {
        return null;
    }

    @Override
    public ResponseObject sendOtp(String email) {
        return null;
    }

    @Override
    public ResponseObject findAllUsersByOffice(UUID officeId, Integer pageNumber, Integer pageSize) {
        return null;
    }
}
