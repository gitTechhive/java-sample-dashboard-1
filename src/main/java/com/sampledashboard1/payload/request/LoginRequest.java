package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginRequest {
    @Email
    //@NotEmpty(message = "Email can't not be null")
    private String email;
   // @NotEmpty(message = "password can't not be null")
    private String password;
    private Long mobileNo;
    private String loginType;

}
