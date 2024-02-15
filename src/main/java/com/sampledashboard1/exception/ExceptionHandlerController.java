package com.sampledashboard1.exception;

import com.sampledashboard1.filter.ResponseWrapperDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;*/
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
@ControllerAdvice
@Validated
@RequiredArgsConstructor
public class ExceptionHandlerController {

    @ExceptionHandler(UserDefineException.class)
    public final ResponseEntity<ResponseWrapperDTO> handleCustomException(UserDefineException ex, HttpServletRequest request) {
        printLogError(ex);
        return new ResponseEntity<>(new ResponseWrapperDTO(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage(), null, request.getServletPath()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public final ResponseEntity<ResponseWrapperDTO> handleTokenRefreshException(TokenRefreshException ex, HttpServletRequest request) {
        printLogError(ex);
        return new ResponseEntity<>(new ResponseWrapperDTO(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), null, request.getServletPath()), HttpStatus.UNAUTHORIZED);
    }

   /* @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public final ResponseEntity<ResponseWrapperDTO> handleBadCredentialsException(Exception ex, HttpServletRequest request) {
        printLogError(ex);
        return ResponseEntity.badRequest().body(new ResponseWrapperDTO(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), null, request.getServletPath()));
    }*/

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public final ResponseEntity<ResponseWrapperDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        printLogError(ex);
        return new ResponseEntity<>(new ResponseWrapperDTO(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File size is too long!", null, request.getServletPath()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ResponseEntity<ResponseWrapperDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        printLogError(ex);
        return new ResponseEntity<>(new ResponseWrapperDTO(HttpServletResponse.SC_BAD_REQUEST, String.format("Required(%s) parameter is missing.", ex.getParameterName()), null, request.getServletPath()), HttpStatus.BAD_REQUEST);
    }
  /*  @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ResponseWrapperDTO> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        printLogError(ex);
        return new ResponseEntity<>(new ResponseWrapperDTO(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), null, request.getServletPath()), HttpStatus.UNAUTHORIZED);
    }*/
    @ExceptionHandler(UserNotActiveException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseWrapperDTO> handleUserNotActiveException(UserNotActiveException e,HttpServletRequest request) {
        printLogError(e);
        return new ResponseEntity<>(new ResponseWrapperDTO(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), "error", request.getServletPath()), HttpStatus.UNAUTHORIZED);
    }
    // field level validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, final HttpServletRequest request) {
        printLogError(ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error("Controller->>> (MethodArgumentNotValidException) handleValidationExceptions MethodArgumentNotValidException Details--> {}", error);
        });

        //* change for Proper Error Message *//*
        StringBuilder errorObj = new StringBuilder();
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            errorObj.append(entry.getValue());
        }

        return ResponseEntity.internalServerError().body(ResponseWrapperDTO.customResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorObj.toString(), null, request));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ResponseWrapperDTO> handleAllException(Exception ex, HttpServletRequest request) {
        printLogError(ex);
        return new ResponseEntity<>(new ResponseWrapperDTO(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), null, request.getServletPath()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * print log error
     */
    private void printLogError(Exception exception) {
        log.error("handle Exception Exception  Detail", exception);
    }

}
