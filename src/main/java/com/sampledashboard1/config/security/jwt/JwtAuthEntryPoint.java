package com.sampledashboard1.config.security.jwt;

import com.sampledashboard1.utils.MethodUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final String expiredMsg = (String) request.getAttribute("TOKEN_INVALID");
        String resMsg = MethodUtils.isObjectisNullOrEmpty(expiredMsg) ? e.getMessage() : expiredMsg;
        log.error("Unauthorized error. Message -{}", resMsg);

         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, resMsg);
    }
}
