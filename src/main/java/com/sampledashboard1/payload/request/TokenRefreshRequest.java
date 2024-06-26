package com.sampledashboard1.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenRefreshRequest {
    @NotEmpty(message = "{tokenRefresh.refreshToken.is.required}")
    private String refreshToken;
}
