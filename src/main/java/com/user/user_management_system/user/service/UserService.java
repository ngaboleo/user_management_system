package com.user.user_management_system.user.service;

import com.user.user_management_system.Message.IMessageService;
import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.notification.INotificationService;
import com.user.user_management_system.notification.NotificationService;
import com.user.user_management_system.office.model.IOfficeRepository;
import com.user.user_management_system.office.model.Office;
import com.user.user_management_system.role.model.IRoleRepository;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.user.auth.UserImplDetailService;
import com.user.user_management_system.user.dto.LoginRequest;
import com.user.user_management_system.user.dto.LoginResponseDto;
import com.user.user_management_system.user.dto.UpdatePasswordDto;
import com.user.user_management_system.user.dto.UserDto;
import com.user.user_management_system.user.model.IUserRepository;
import com.user.user_management_system.user.model.User;
import com.user.user_management_system.util.PageObject;
import com.user.user_management_system.util.ResponseObject;
import com.user.user_management_system.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class UserService implements IUserService{
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IOfficeRepository iOfficeRepository;
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserImplDetailService userImplDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private NotificationService notificationService;
    private Set<Role> roles = new HashSet<>();



    @Override
    public ResponseObject createUser(UserDto userDto) {
        // validate user given data
        // find user using email or phone and check if they exist
        // find office provided by user and check if it is available or present in database
        // set user data to user object and save
        try {
            if (validateUserDto(userDto)){
                List<User> users = iUserRepository.findUsersByEmailOrPhoneNumber(userDto.getEmail(), userDto.getPhoneNumber());
                if (users.isEmpty()){
                    Optional<Office> optionalOffice = iOfficeRepository.findById(userDto.getOfficeId());
                    if (optionalOffice.isPresent()){
                        for (UUID roleId: userDto.getRoles()) {
                            Optional<Role> optionalRole = iRoleRepository.findById(roleId);
                            roles.add(optionalRole.get());
                        }
                        User currentUser = new User();
                        BeanUtils.copyProperties(userDto, currentUser);
                        currentUser.setIsActive(false);
                        currentUser.setOffice(optionalOffice.get());
                        currentUser.setRoles(roles);
                        String randomPassword = generateRandomSpecialCharacters(10);
                        System.out.println(randomPassword);
                        currentUser.setPassword(passwordEncoder.encode(randomPassword));
                        User userSaved = iUserRepository.save(currentUser);

                        // to do message and send email
                        notificationService.sendEmail(userDto.getEmail(),"activate the account", "temporary password"+randomPassword);


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

    public String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
                .build();
        return pwdGenerator.generate(length);
    }

    private boolean validateUserDto(UserDto userDto) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return  ObjectUtils.isNotEmpty(userDto.getFullName()) &&  ObjectUtils.isNotEmpty(userDto.getRoles())
                && ObjectUtils.isNotEmpty(userDto.getDocumentType())
                && ObjectUtils.isNotEmpty(userDto.getJurisdictionLevel())
                && emailValidator.isValid(userDto.getEmail())
                && ObjectUtils.isNotEmpty(userDto.getPhoneNumber()) && userDto.getPhoneNumber().length() == 10;
    }

    @Override
    public ResponseObject login(LoginRequest loginRequest) {
        try {
            authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            final UserDetails userDetails = userImplDetailService.loadUserByUsername(loginRequest.getEmail());

            //extract user info
            Optional<User> optionalUser = iUserRepository.findUserByEmailIgnoreCase(loginRequest.getEmail());
            if (optionalUser.isPresent()){
                User loggedUser = optionalUser.get();
                if (loggedUser.getIsActive()){
                    final String token = tokenUtil.generateToken(userDetails);
                    return new ResponseObject(new LoginResponseDto(token, loggedUser));
                }else {
                    return new ResponseObject("Please active this account!!" +loggedUser.getEmail());
                }
            }else {
                throw new HandleException("user not found");
            }
        }catch (Exception ex){
            throw new HandleException(ex);
        }
    }

    @Override
    public ResponseObject activateAccount(String email, String password) {
        try {
            Optional<User> optionalUser = iUserRepository.findUserByEmailIgnoreCase(email);
            User user = optionalUser.get();
            if (!user.getIsActive()){
                user.setPassword(passwordEncoder.encode(password));
                user.setIsActive(true);
                iUserRepository.save(user);
            }
            return login(new LoginRequest(email, password));
        }catch (Exception exception){
            throw new HandleException(exception);
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
    public ResponseObject editUser(UUID userId, UserDto userDto) {
        try {
            Optional<User> optionalUser = iUserRepository.findUserById(userId);
            User user = optionalUser.get();
            if (user.getIsActive()){
                user.setPhoneNumber(userDto.getPhoneNumber());
                user.setJurisdictionLevel(userDto.getJurisdictionLevel());
                for (UUID roleId: userDto.getRoles()) {
                    Optional<Role> optionalRole = iRoleRepository.findById(roleId);
                    roles.add(optionalRole.get());
                }
                user.setRoles(roles);
            }else {
                user.setEmail(userDto.getEmail());
                user.setDocumentId(userDto.getDocumentId());
            }
            return new ResponseObject(iUserRepository.save(user));
        }catch (Exception exception){
            throw new HandleException(exception);
        }

    }

    @Override
    public ResponseObject getAllUser(Integer pageNumber, Integer pageSize) {
        try {
            Page<User> users = iUserRepository.findAll(PageObject.getPageable(pageNumber, pageSize));
            return new ResponseObject(users);
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject findUserById(UUID id) {
        return new ResponseObject(iUserRepository.findUserById(id).orElseThrow(() -> new HandleException(IMessageService.USER_NOT_FOUND)));
    }

    @Override
    public ResponseObject findUsersByRole(UUID roleId) {
        try {
            return new ResponseObject(iUserRepository.findUsersByRolesId(roleId));
        }catch (Exception exception){
            throw new HandleException(IMessageService.ROLE_NOT_FOUND);
        }
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
