package com.sampledashboard1.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long mobileNo;
    private String token;
}
