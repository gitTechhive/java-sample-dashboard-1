package com.sampledashboard1.service;

public interface LoginService {

    String forgotPwdSendEmail(String email);

    String forgotPwdOtpVerification(String email,String otp);

    String forgotPwd(String email,String password);
}
