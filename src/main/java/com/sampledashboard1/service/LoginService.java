package com.sampledashboard1.service;

import com.sampledashboard1.payload.response.LoginResponse;

import java.util.Map;

public interface LoginService {

    Map<String,String> forgotPwdSendEmail(String email);

    String forgotPwdOtpVerification(String email,String otp,String requestId);

    String forgotPwd(String email,String password);
    String changePwd(String crnPass, String pwd);
    LoginResponse loginPhoneNo(String phoneNo, String otp,String countryCode,String requestId);

    String sendOtpLoginPhoneNo(String phoneNo,String uuid,String code);
}
