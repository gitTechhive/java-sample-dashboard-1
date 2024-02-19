package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChangePassRequest {
    @NotEmpty(message = "{changePass.crnPass.is.required}")
    String crnPass;
    @NotEmpty(message = "{changePass.pwd.is.required}")
    String pwd;
}
