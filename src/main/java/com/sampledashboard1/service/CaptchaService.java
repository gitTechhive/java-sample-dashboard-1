package com.sampledashboard1.service;

import com.sampledashboard1.model.Captcha;

import java.time.LocalDate;

public interface CaptchaService {
    Captcha generateCaptcha();

    Captcha reGenerateCaptcha(String uuid);
    String verificationCaptcha(String uuid,String hiddenCaptcha);

    String deleteCaptcha(LocalDate date);


}
