package com.sampledashboard1.controller;

import com.sampledashboard1.filter.ResponseWrapperDTO;
import com.sampledashboard1.payload.request.MailRequest;
import com.sampledashboard1.payload.request.SaveUsersRequest;
import com.sampledashboard1.payload.request.SendOtpRequest;
import com.sampledashboard1.payload.request.SignUpGoogleRequest;
import com.sampledashboard1.service.EmailService;
import com.sampledashboard1.service.OtpVerificationService;
import com.sampledashboard1.service.UsersService;
import com.sampledashboard1.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UsersController {

    private final UsersService usersService;
    private  final EmailService emailService;
    private  final OtpVerificationService otpVerificationService;

    /**
     * This API Used For OTP verification if(verification is done then after save User)
     *
     * @param saveUsersData
     * @param httpServletRequest
     *
     */
    @PostMapping("/")
    public ResponseWrapperDTO otpVerification(@Valid @RequestBody SaveUsersRequest saveUsersData,
                                        HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.save"), usersService.otpVerification(saveUsersData), httpServletRequest);
    }
    @PostMapping("/singUpGoogle")
    public ResponseWrapperDTO singUpGoogle(@Valid @RequestBody SignUpGoogleRequest request,
                                                HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.save"), usersService.signUpGoogle(request), httpServletRequest);
    }

    /**
     * This API Used For send email
     * @param mailRequest
     * @param httpServletRequest
     *
     */
    @PostMapping("send")
    public ResponseWrapperDTO sendEmail( @RequestBody MailRequest mailRequest, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse("hello send done", emailService.sendEmail(mailRequest), httpServletRequest);
    }

    /**
     *  This API Used For send OTP in email verification
     * @param sendOtpRequest
     * @param httpServletRequest
     *
     */

    @PostMapping("/sendOtp")
    public ResponseWrapperDTO sendOtp(@RequestBody SaveUsersRequest sendOtpRequest, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse("OTP Send Successfully", otpVerificationService.sendOtp(sendOtpRequest), httpServletRequest);
    }


    @GetMapping("/hello")
    public String welcome() {
        return "java-sample-dashboard-1 ";
    }
}
