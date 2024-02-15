package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.Login;
import com.sampledashboard1.model.OtpVerification;
import com.sampledashboard1.payload.request.MailRequest;
import com.sampledashboard1.repository.LoginRepository;
import com.sampledashboard1.repository.OtpVerificationRepository;
import com.sampledashboard1.service.LoginService;
import com.sampledashboard1.utils.MessageUtils;
import com.sampledashboard1.utils.MethodUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final OtpVerificationRepository otpVerificationRepository;
    private final EmailServiceImpl emailService;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String forgotPwdSendEmail(String email) {
        OtpVerification otpVerification = otpVerificationRepository.findByRequestValue(email);
        if(otpVerification == null){
            throw new UserDefineException("Please enter registered email.");
        }
        String s = MethodUtils.generateRandomInteger(6);
        otpVerification.setOtp(s);
        String string = UUID.randomUUID().toString();
        otpVerification.setRequestId(string);
        LocalDateTime currentDateTime = LocalDateTime.now();
        otpVerification.setOtpExpiredOn(currentDateTime.plusMinutes(10));
        emailService.sendEmail(MailRequest.builder()
                .to(email)
                .subject("SampleDashBoard Verify Email")
                .body("Your Request Id Is : "+string + "And  OTP Is : " + s)
                .build());
        otpVerificationRepository.save(otpVerification);
        return "Please check mail.";
    }

    @Override
    public String forgotPwdOtpVerification(String email, String otp) {
        OtpVerification otpVerification = otpVerificationRepository.getDataByEmailOrOtp(email, otp);
        if(otpVerification == null){
            throw new UserDefineException("Please enter valid otp.");
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        if(otpVerification.getOtpExpiredOn().compareTo(currentDateTime) <= 0){
            throw new UserDefineException("Please Resend otp.");
        }
        return "OTP verify successfully";
    }

    @Override
    public String forgotPwd(String email, String password) {
        Login login = loginRepository.findByEmail(email).orElseThrow(() -> new UserDefineException(MessageUtils.get("login.service.val.registered.email")));
        login.setPassword(passwordEncoder.encode(password));
        loginRepository.save(login);
        return "password save successfully";
    }
}
