package com.sampledashboard1.service;

import com.sampledashboard1.payload.response.LoginResponse;

public interface LoginService {

    String forgotPwdSendEmail(String email);

    String forgotPwdOtpVerification(String email,String otp);

    String forgotPwd(String email,String password);
    String changePwd(String crnPass, String pwd);
    LoginResponse loginPhoneNo(String phoneNo, String otp);

    String sendOtpLoginPhoneNo(String phoneNo);
}
