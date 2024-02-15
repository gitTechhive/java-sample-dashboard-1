package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SaveUsersRequest {
    private Long id;
    @NotEmpty(message = "{users.firstName.is.required}")
    private String firstName;
    @NotEmpty(message = "{users.lastName.is.required}")
    private String lastName;
    private String address;
    private String country;
    private String state;
    private String pinCode;
    @Email(message = "{users.email.is.required}")
    private String email;
    @NotNull(message = "{users.mobileNo.is.required}")
    private Long mobileNo;
    @NotNull(message = "{users.otp.is.required}")
    private String otp;
    @NotNull(message = "{users.requestId.is.required}")
    private String requestId;
}