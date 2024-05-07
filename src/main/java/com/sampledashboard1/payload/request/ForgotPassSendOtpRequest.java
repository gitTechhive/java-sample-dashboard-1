package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ForgotPassSendOtpRequest {
    @NotEmpty(message = "{forgotPass.token.is.required}")
    String email;
    String otp;
    String requestId;
}
