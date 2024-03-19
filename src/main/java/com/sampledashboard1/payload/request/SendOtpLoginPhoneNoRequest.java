package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SendOtpLoginPhoneNoRequest {
    private String phoneNo;
    private String uuid;
    private String countryCode;
}
