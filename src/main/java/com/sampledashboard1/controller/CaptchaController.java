package com.sampledashboard1.controller;

import com.sampledashboard1.filter.ResponseWrapperDTO;
import com.sampledashboard1.payload.request.CaptchaRequest;
import com.sampledashboard1.service.CaptchaService;
import com.sampledashboard1.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Operation( description = "This API Used for generate Captcha")
    @GetMapping("/generate")
    public ResponseWrapperDTO generateCaptcha(HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("captcha.controller.generateCaptcha"), captchaService.generateCaptcha(), httpServletRequest);
    }

    /**
     * This API Used for Re-Generate Captcha

     * @param httpServletRequest
     * @return
     */
    @Operation( description = "This API Used for Re-Generate Captcha")
    @PostMapping("/reGenerate")
    public ResponseWrapperDTO reGenerateCaptcha(@RequestBody CaptchaRequest captchaRequest, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("captcha.controller.generateCaptcha"), captchaService.reGenerateCaptcha(captchaRequest.getUuId()), httpServletRequest);
    }

    /**
     * This API used for Captcha verification
     * @param httpServletRequest
     * @return
     */
    @Operation( description = "This API used for Captcha verification")
    @PostMapping("/verification")
    public ResponseWrapperDTO verificationCaptcha(@RequestBody CaptchaRequest request, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("captcha.controller.verificationCaptcha"), captchaService.verificationCaptcha(request.getUuId(), request.getHiddenCaptcha()), httpServletRequest);
    }
}
