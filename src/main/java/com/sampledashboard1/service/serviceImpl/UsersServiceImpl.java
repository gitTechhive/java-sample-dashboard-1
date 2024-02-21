package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.config.security.CustomUserDetailsService;
import com.sampledashboard1.config.security.jwt.JwtProvider;
import com.sampledashboard1.enums.EnumForLoginType;
import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.*;
import com.sampledashboard1.payload.request.MailRequest;
import com.sampledashboard1.payload.request.SaveUsersRequest;
import com.sampledashboard1.payload.request.SignUpGoogleRequest;
import com.sampledashboard1.payload.response.OtpVerificationResponse;
import com.sampledashboard1.payload.response.SignUpGoogleResponse;
import com.sampledashboard1.repository.*;
import com.sampledashboard1.service.OtpVerificationService;
import com.sampledashboard1.service.UsersService;
import com.sampledashboard1.utils.MessageUtils;
import com.sampledashboard1.utils.MethodUtils;
import com.sampledashboard1.validation.UsersValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final LoginRepository loginRepository;
    private final UsersValidation usersValidation;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final CountriesRepository countriesRepository;
    private final StatesRepository statesRepository;
    private final CitiesRepository citiesRepository;
    private final UserDocsRepository userDocsRepository;
    public void validationForSaveUsers(SaveUsersRequest request) {
        if (request != null && request.getId() != null) {
            usersValidation.checkEmailIsExits(request.getEmail(), request.getId());
            usersValidation.checkMobileNoIsExits(request.getMobileNo(), request.getId());
        } else {
            usersValidation.checkEmailIsExits(request.getEmail(), null);
            usersValidation.checkMobileNoIsExits(request.getMobileNo(), null);
        }
    }

    private void mapRequestToUser(SaveUsersRequest request, Users users, Login login) {
        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setAddress(request.getAddress());
     //   users.setCountry(request.getCountry());
        users.setPinCode(request.getPinCode());
       // users.setState(request.getState());
        users.setMobileNo(request.getMobileNo());
        users.setLogin(login);
    }

    private void mapRequestToLogin(SaveUsersRequest request, Login login) {
        login.setEmail(request.getEmail());
    }

    @Override
    @Transactional
    public OtpVerificationResponse otpVerification(SaveUsersRequest request) {

        Boolean flag = validationForSaveUsers(request.getRequestId());

        if (Boolean.FALSE.equals(flag)) {
            throw new UserDefineException("OTP Invalid");
        }
        Users users = null;
        Login login = null;
        if (request != null && request.getId() != null) {
            users = usersRepository.findById(request.getId()).orElseThrow(() -> new UserDefineException(MessageUtils.get("users.not.found")));
            login = users.getLogin();
        } else {
            users = new Users();
            login = new Login();
        }
        validationForSaveUsers(request);
        mapRequestToLogin(request, login);
        String s = MethodUtils.generateRandomString(8);
        login.setPassword(passwordEncoder.encode(s).toString());

        mapRequestToUser(request, users, login);
        loginRepository.saveAndFlush(login);
        usersRepository.saveAndFlush(users);
        emailService.sendEmail(MailRequest.builder()
                .to(login.getEmail())
                .subject("password verification")
                .body("Your password is : " + s)
                .build());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtProvider.generateToken(userDetails);
        OtpVerificationResponse.OtpVerificationResponseBuilder responseBuilder = OtpVerificationResponse.builder()
                .id(users.getId())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .token(jwt)
                .mobileNo(users.getMobileNo())
                .email(login.getEmail());

        return responseBuilder.build();
    }

    @Override
    public SignUpGoogleResponse signUpGoogle(SignUpGoogleRequest request) {
        if(request != null && (request.getEmail() != null|| !request.getEmail().isEmpty())){

            Optional<Login> byEmail = loginRepository.findByEmail(request.getEmail());
            if(!byEmail.isEmpty()){
                throw new UserDefineException(MessageUtils.get("users.validation.email"));
            }

            Login login=new Login();
            Users users =new Users();

            login.setEmail(request.getEmail());
            login.setGoogleId(request.getGoogleId());
            users.setFirstName(request.getFirstName());
            users.setLastName(request.getLastName());
            if(request.getType().equals(EnumForLoginType.Google.value())){
                users.setType(EnumForLoginType.Google.value());
            }
            loginRepository.saveAndFlush(login);
            Login login1 = loginRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserDefineException("Login not found."));
            users.setLogin(login1);
            usersRepository.saveAndFlush(users);
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String jwt = jwtProvider.generateToken(userDetails);
            SignUpGoogleResponse.SignUpGoogleResponseBuilder responseBuilder = SignUpGoogleResponse.builder()
                    .id(users.getId())
                    .firstName(users.getFirstName())
                    .lastName(users.getLastName())
                    .token(jwt)
                    .email(login.getEmail());
            return  responseBuilder.build();
        }
        return null;
    }

    @Override
    @Transactional
    public String updateUsers(SaveUsersRequest request) {
        Long userIdByToken = Long.valueOf(MethodUtils.getCurrentUserId());
        Users users=usersRepository.findById(userIdByToken).orElseThrow(()-> new UserDefineException("User not found."));
        usersValidation.checkUserUpdateValidation(request, users.getId());
        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setAddress(request.getAddress());
        users.setPinCode(request.getPinCode());
        users.setMobileNo(request.getMobileNo());
        //Email update
        Login login = loginRepository.findById(users.getLogin().getId()).orElseThrow(() -> new UserDefineException("Login not found."));
        login.setEmail(request.getEmail());

        users.setBio(request.getBio());
        users.setPhoneCode(request.getPhonecode());
        Countries countries = countriesRepository.findById(request.getCountry_id()).orElseThrow(() -> new UserDefineException("Countries not found."));
        users.setCountry(countries);
        States states = statesRepository.findById(request.getState_id()).orElseThrow(() -> new UserDefineException("State not found."));
        users.setState(states);
        Cities cities = citiesRepository.findById(request.getCities_id()).orElseThrow(() -> new UserDefineException("Cities not found."));
        users.setCities(cities);
        loginRepository.save(login);
        usersRepository.save(users);

        return MessageUtils.get("users.controller.update");
    }

    @Override
    public Users getUsers() {
        Long userIdByToken = Long.valueOf(MethodUtils.getCurrentUserId());
        Users user=usersRepository.getUsersData(userIdByToken);
        return user;
    }

    public Boolean validationForSaveUsers( String requestId) {
        Boolean flag = false;
        OtpVerification dataByEmailOrOtpAndUId = otpVerificationRepository.getDataByUId(requestId);
        if(dataByEmailOrOtpAndUId != null){
            flag = true;
        }
        return flag;
    }
    @Override
    @Transactional
    public void deleteUserDoc( Long userId) {
        userDocsRepository.deleteUserDocByUserId(userId);
    }

}
