package com.sampledashboard1.service;

import com.sampledashboard1.model.Users;
import com.sampledashboard1.payload.request.SaveUsersRequest;
import com.sampledashboard1.payload.request.SignUpGoogleRequest;
import com.sampledashboard1.payload.response.OtpVerificationResponse;
import com.sampledashboard1.payload.response.SignUpGoogleResponse;
import org.apache.catalina.User;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UsersService {
    OtpVerificationResponse otpVerification(SaveUsersRequest request);
    SignUpGoogleResponse signUpGoogle(SignUpGoogleRequest request);
    String  updateUsers (SaveUsersRequest request);
    Users getUsers ();
    void deleteUserDoc( Long userId);


}
