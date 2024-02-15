package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SendOtpRequest {

    private String email;
    private Long mobileNo;
}
