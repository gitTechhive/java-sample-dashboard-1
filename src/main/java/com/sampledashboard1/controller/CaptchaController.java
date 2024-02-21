package com.sampledashboard1.controller;

import com.sampledashboard1.filter.ResponseWrapperDTO;
import com.sampledashboard1.service.CaptchaService;
import com.sampledashboard1.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("captcha")
public class CaptchaController {

    private final CaptchaService  captchaService;

    /**
     * This API Used for generate Captcha
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/generate")
    public ResponseWrapperDTO generateCaptcha( HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("captcha.controller.generateCaptcha"), captchaService.generateCaptcha(), httpServletRequest);
    }

    /**
     * This API Used for Re-Generate Captcha
     * @param uuId
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/reGenerate")
    public ResponseWrapperDTO reGenerateCaptcha(@RequestParam String uuId, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("captcha.controller.generateCaptcha"), captchaService.reGenerateCaptcha(uuId), httpServletRequest);
    }

    /**
     * This API used for Captcha verification
     * @param uuId
     * @param hiddenCaptcha
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/verification")
    public ResponseWrapperDTO verificationCaptcha(@RequestParam String uuId,@RequestParam String hiddenCaptcha, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("captcha.controller.verificationCaptcha"), captchaService.verificationCaptcha(uuId,hiddenCaptcha), httpServletRequest);
    }
}
