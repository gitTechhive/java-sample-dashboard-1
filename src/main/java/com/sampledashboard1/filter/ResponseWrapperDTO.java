package com.sampledashboard1.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class ResponseWrapperDTO {
    @JsonFormat(pattern = VariableUtils.DATE_TIME_FORMAT)
    private LocalDateTime timeStamp = LocalDateTime.now();

    private Integer status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Object data;
    private String error;
    private String path;

    public ResponseWrapperDTO(Integer status, String error, String path) {
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public ResponseWrapperDTO(Integer status, String message, Object data, String path) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.path = path;
    }

    public static ResponseWrapperDTO customResponse(Integer status, String message, Object data, HttpServletRequest request) {
        return new ResponseWrapperDTO(status, message, data, request.getServletPath());
    }

    public static ResponseWrapperDTO successResponse(String message, Object data, HttpServletRequest request) {
        return new ResponseWrapperDTO(HttpServletResponse.SC_OK, message, data, request.getServletPath());
    }

    public static ResponseWrapperDTO errorResponse(Integer status, String error, HttpServletRequest request) {
        return new ResponseWrapperDTO(status, error, request.getServletPath());
    }

}
