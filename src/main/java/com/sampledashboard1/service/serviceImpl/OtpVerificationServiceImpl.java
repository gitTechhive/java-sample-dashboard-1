package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.config.security.CustomUserDetailsService;
import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.OtpVerification;
import com.sampledashboard1.payload.request.MailRequest;
import com.sampledashboard1.payload.request.SaveUsersRequest;
import com.sampledashboard1.payload.request.SendOtpRequest;
import com.sampledashboard1.repository.OtpVerificationRepository;
import com.sampledashboard1.service.OtpVerificationService;
import com.sampledashboard1.utils.MethodUtils;
import com.sampledashboard1.validation.UsersValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OtpVerificationServiceImpl implements OtpVerificationService {

    private final EmailServiceImpl emailService;
    private final UsersValidation usersValidation;

    private final OtpVerificationRepository otpVerificationRepository;

    @Override
    public OtpVerification sendOtp(SaveUsersRequest sendOtpRequest) {
        OtpVerification otpVerification = null ;
        if(sendOtpRequest != null && ( !sendOtpRequest.getEmail().isEmpty() ||sendOtpRequest.getEmail() != null)){
            usersValidation.checkEmailIsExits(sendOtpRequest.getEmail(), null);
            OtpVerification byEmail = otpVerificationRepository.findByRequestValue(sendOtpRequest.getEmail());
            if(byEmail != null){
                otpVerification=byEmail;
            }else{
                otpVerification = new OtpVerification();
            }
        }
        if(sendOtpRequest != null && ( sendOtpRequest.getMobileNo() != null)){
            usersValidation.checkMobileNoIsExits(sendOtpRequest.getMobileNo(), null);
        }
        String s = MethodUtils.generateRandomInteger(6);
        if (sendOtpRequest != null && ( !sendOtpRequest.getEmail().isEmpty())) {
            otpVerification.setRequestType("email");
            otpVerification.setRequestValue(sendOtpRequest.getEmail());
        }
        otpVerification.setRequestId(UUID.randomUUID().toString());
        otpVerification.setOtp(s);


        emailService.sendEmail(MailRequest.builder()
                .to(sendOtpRequest.getEmail())
                .subject("SampleDashBoard Verify Email")
                .body("Your OTP is : " + s)
                .build());
        otpVerificationRepository.save(otpVerification);
        return new OtpVerification(otpVerification.getRequestId());
    }


}
