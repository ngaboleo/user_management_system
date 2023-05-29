package com.user.user_management_system.user.service;

import com.user.user_management_system.Message.IMessageService;
import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.notification.IEmailSenderService;
import com.user.user_management_system.office.model.IOfficeRepository;
import com.user.user_management_system.office.model.Office;
import com.user.user_management_system.role.model.IRoleRepository;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.user.auth.UserImplDetailService;
import com.user.user_management_system.user.dto.*;
import com.user.user_management_system.user.model.IResetLinkerRepository;
import com.user.user_management_system.user.model.IUserRepository;
import com.user.user_management_system.user.model.ResetLinker;
import com.user.user_management_system.user.model.User;
import com.user.user_management_system.util.PageObject;
import com.user.user_management_system.util.ResponseObject;
import com.user.user_management_system.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.swing.*;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.random.RandomGenerator;

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
    private IEmailSenderService iEmailSenderService;
    @Autowired
    private IResetLinkerRepository iResetLinkerRepository;
    private Set<Role> roles = new HashSet<>();

    @Value("${expirationDate}")
    private Long expirationDate;
    @Value("${otpExpirationTime}")
    private Long otpTime;



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
                        String randomPassword = RandomStringUtils.randomAlphabetic(26);
                        System.out.println(randomPassword);
                        currentUser.setPassword(passwordEncoder.encode(randomPassword));
                        User userSaved = iUserRepository.save(currentUser);

                        // to do message and send email
//                        notificationService.sendEmail(userDto.getEmail(),"activate the account", "temporary password"+randomPassword);
                        iEmailSenderService.sendEmail(userDto.getEmail(),"activate the account", "temporary password: "+randomPassword);

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
                        sendOtp(loggedUser.getEmail());
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
    public ResponseObject resetPassword(String email) {
        try {
            Optional<User> optionalUser = iUserRepository.findUserByEmailIgnoreCase(email);
            if (optionalUser.isPresent()){
                ResetLinker resetLinker = new ResetLinker();
//                random string for token generate
                String token = RandomStringUtils.randomAlphabetic(26);
                resetLinker.setToken(token.toString());
                resetLinker.setUser(optionalUser.get());
                iResetLinkerRepository.save(resetLinker);
                iEmailSenderService.sendEmail(email,"Reset password link",
                        "http://localhost:8080/confirm-account?token= " +resetLinker.getToken());
                return new ResponseObject(resetLinker);

            }else {
                throw new HandleException(IMessageService.USER_NOT_FOUND);
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject changePassword(String token, String newPassword) {
        try {
            Optional<ResetLinker> resetLinkerOptional = iResetLinkerRepository.findResetLinkerByTokenIgnoreCase(token);
            Date createAt = resetLinkerOptional.get().getCreatedAt();
            Date expirationForToken = Date.from(createAt.toInstant().plus(Duration.ofMinutes(expirationDate)));
            LocalDateTime currentDateTime = LocalDateTime.now();
            Date currentTime = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
            if (resetLinkerOptional.isPresent()){
                if (!expirationForToken.before(currentTime)){
                    Optional<User> userOptional = iUserRepository.findUserById(resetLinkerOptional.get().getUser().getId());
                    User user = userOptional.get();
                    user.setPassword(passwordEncoder.encode(newPassword));
                    iUserRepository.save(user);
                    iEmailSenderService.sendEmail(user.getEmail(), "Password Reset successfully", "Dear " +user.getFullName()+", your password has been reset successfully");
                    return new ResponseObject(user);
                }else {
                    throw new HandleException(IMessageService.TOKEN_NOT_VALID);
                }
            }else {
                throw new HandleException(IMessageService.TOKEN_NOT_VALID);
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject userSearch(String keyword) {
        try {
            List<User> userSearch = iUserRepository.findByFullNameContainingIgnoreCase(keyword);
            return new ResponseObject(userSearch);
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject sendOtp(String email) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Date currentTime = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
            User user = iUserRepository.findUserByEmailIgnoreCase(email).orElseThrow(() -> new HandleException(IMessageService.USER_NOT_FOUND));
            if (ObjectUtils.isNotEmpty(user.getEmail())){
                user.setOtp(String.valueOf(this.generateOtp()));
                iUserRepository.save(user);
                iEmailSenderService.sendEmail(user.getEmail(), "otp ", "the one time password you have to use: " +user.getOtp());
                return new ResponseObject(user);
            }else {
                throw new HandleException(IMessageService.USERNAME_NOT_FOUND);
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject verifyOtp(String email, String otp) {
        try {
            User user = iUserRepository.findUserByEmailIgnoreCase(email).orElseThrow(() -> new HandleException(IMessageService.USER_NOT_FOUND));
            final UserDetails userDetails = userImplDetailService.loadUserByUsername(email);
            Date updateAt = user.getUpdateAt();
            Date otpExpirationTime = Date.from(updateAt.toInstant().plus(Duration.ofMinutes(otpTime)));
            LocalDateTime currentDateTime = LocalDateTime.now();
            Date currentTime = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
            if (user.getOtp().equals(otp) && !otpExpirationTime.before(currentTime)){
                    final String token = tokenUtil.generateToken(userDetails);
                    return new ResponseObject(new LoginResponseDto(token, user));
            }else {
                throw new HandleException(IMessageService.OTP_IS_NOT_VALID);
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    private int generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        return (secureRandom.nextInt(899999) + 100000);
    }

    @Override
    public ResponseObject findAllUsersByOffice(UUID officeId, Integer pageNumber, Integer pageSize) {
        return null;
    }
}
