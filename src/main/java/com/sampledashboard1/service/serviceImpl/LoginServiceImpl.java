package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.config.security.jwt.JwtProvider;
import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.*;
import com.sampledashboard1.payload.request.MailRequest;
import com.sampledashboard1.payload.response.LoginResponse;
import com.sampledashboard1.repository.CaptchaRepository;
import com.sampledashboard1.repository.LoginRepository;
import com.sampledashboard1.repository.OtpVerificationRepository;
import com.sampledashboard1.repository.UsersRepository;
import com.sampledashboard1.service.LoginService;
import com.sampledashboard1.service.RefreshTokenService;
import com.sampledashboard1.utils.MessageUtils;
import com.sampledashboard1.utils.MethodUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final OtpVerificationRepository otpVerificationRepository;
    private final EmailServiceImpl emailService;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UsersRepository usersRepository;
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final CaptchaRepository captchaRepository;

    @Override
    public Map<String,String> forgotPwdSendEmail(String email) {
        Map<String,String> map=new HashMap<>();
      //  OtpVerification otpVerification = otpVerificationRepository.findByRequestValue(email);

        Login login =loginRepository.findByEmail(email).orElseThrow(()-> new UserDefineException("Email not found."));
        if (login == null) {
            throw new UserDefineException("Email not found.");
        }
        OtpVerification otpVerification=new OtpVerification();
        String s = MethodUtils.generateRandomInteger(6);
        otpVerification.setOtp(s);
        String requestId = UUID.randomUUID().toString();
        otpVerification.setRequestId(requestId);
        otpVerification.setRequestType("email");
        otpVerification.setRequestValue(email);
        LocalDateTime currentDateTime = LocalDateTime.now();
        otpVerification.setOtpExpiredOn(currentDateTime.plusMinutes(10));
        emailService.sendEmail(MailRequest.builder()
                .to(email)
                .subject("SampleDashBoard Verify Email")
               // .body("Your Request Id Is : " + requestId + "And  OTP Is : " + s)
                .body("OTP Is : " + s)
                .build());
        otpVerificationRepository.save(otpVerification);
        map.put("requestId",requestId);
        return map;
    }

    @Override
    public String forgotPwdOtpVerification(String email, String otp,String requestId) {
        OtpVerification otpVerification = otpVerificationRepository.getDataByEmailOrOtp(email, otp,requestId);
        if (otpVerification == null) {
            throw new UserDefineException("Please enter valid otp.");
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        int i = otpVerification.getOtpExpiredOn().compareTo(currentDateTime);
//        if (otpVerification.getOtpExpiredOn().compareTo(currentDateTime) <= 0) {
//            throw new UserDefineException("Please Resend otp.");
//        }
        if (i<0) {
            throw new UserDefineException("Please Resend otp.");
        }
        return MessageUtils.get("login.service.val.OtpVerification");
    }

    @Override
    public String forgotPwd(String email, String password) {
        Login login = loginRepository.findByEmail(email).orElseThrow(() -> new UserDefineException(MessageUtils.get("login.service.val.registered.email")));
        login.setPassword(passwordEncoder.encode(password));
        loginRepository.save(login);
        return MessageUtils.get("login.service.change.pwd");
    }
    @Override
    @Transactional
    public String changePwd(String crnPass, String pwd) {
        String currentLoginId = MethodUtils.getCurrentLoginId();
        Login login = loginRepository.findById(Long.valueOf(currentLoginId)).orElseThrow(() -> new UserDefineException(MessageUtils.get("login.not.found")));
        if (Boolean.FALSE.equals(passwordEncoder.matches(crnPass, login.getPassword()))) {
            throw new UserDefineException(MessageUtils.get("login.service.val.pwd.matched"));
        }
        login.setPassword(passwordEncoder.encode(pwd));
        loginRepository.save(login);
        return MessageUtils.get("login.service.change.pwd");
    }
    @Override
    public LoginResponse loginPhoneNo(String phoneNo, String otp,String countryCode) {

        //otp verification
        OtpVerification dataByPhoneNoOrOtp = otpVerificationRepository.getDataByPhoneNoOrOtp(phoneNo, otp);
        if (dataByPhoneNoOrOtp == null) {
            throw new UserDefineException("Invalid OTP");
        }
        //otp successfully then after
        Users users1 = usersRepository.getUserByMobileNo(Long.valueOf(phoneNo)).orElseThrow(() -> new UserDefineException("Mobile Number not Found !"));


        Login login = loginRepository.findById(users1.getLogin().getId()).orElseThrow(() -> new UserDefineException("user not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(login.getEmail());
        String jwt = jwtProvider.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(login.getId());

        Users users = usersRepository.getUserByLoginId(login.getId()).orElse(null);
        LoginResponse.LoginResponseBuilder responseBuilder = LoginResponse.builder()
                .id(login.getId())
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .email(login.getEmail());

        if (!MethodUtils.isObjectisNullOrEmpty(users)) {
            assert users != null;
            responseBuilder.firstName(users.getFirstName())
                    .userId(users.getId())
                    .lastName(users.getLastName());
        }
        return  responseBuilder.build();
    }

    @Override
    public String sendOtpLoginPhoneNo(String phoneNo,String uuid,String code) {

        Captcha dataByUID = captchaRepository.getDataByUID(uuid);
        LocalDateTime currentDateTime = LocalDateTime.now();
        int i = dataByUID.getExpiryTimestamp().compareTo(currentDateTime);
        if(i<0 ){
            throw new UserDefineException("Your Captcha Expire.");
        }
        if((Boolean.FALSE.equals( dataByUID.getIsVerified())) || dataByUID.getIsVerified() == null ){
            throw new UserDefineException("Your Captcha Not Verified");
        }

        Users users1 = usersRepository.getUserByMobileNo(Long.valueOf(phoneNo)).orElseThrow(() -> new UserDefineException("Mobile Number not Found !"));
        OtpVerification otpVerification=null;
        OtpVerification dataByPhoneNo = otpVerificationRepository.getDataByPhoneNo(phoneNo);
        if(dataByPhoneNo != null){
            otpVerification=dataByPhoneNo;
        }else{
            otpVerification =new OtpVerification();
        }
        otpVerification.setRequestType("mobile");
        otpVerification.setRequestValue(phoneNo);
        //set static otp send  -> 123456
        otpVerification.setOtp("123456");
        otpVerificationRepository.save(otpVerification);
        return MessageUtils.get("login.service.val.OtpSend");
    }

}
