package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignUpGoogleRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String googleId;
    private String type;
  //  private String uuid;
}
