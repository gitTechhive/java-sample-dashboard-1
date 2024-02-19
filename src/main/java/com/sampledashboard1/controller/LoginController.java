package com.sampledashboard1.controller;

import com.sampledashboard1.config.security.CustomUserDetailsService;
import com.sampledashboard1.config.security.jwt.JwtProvider;
import com.sampledashboard1.enums.EnumForLoginType;
import com.sampledashboard1.exception.TokenRefreshException;
import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.filter.ResponseWrapperDTO;
import com.sampledashboard1.model.Login;
import com.sampledashboard1.model.RefreshToken;
import com.sampledashboard1.model.Users;
import com.sampledashboard1.payload.request.*;
import com.sampledashboard1.payload.response.LoginResponse;
import com.sampledashboard1.payload.response.TokenRefreshResponse;
import com.sampledashboard1.repository.LoginRepository;
import com.sampledashboard1.repository.UsersRepository;
import com.sampledashboard1.service.LoginService;
import com.sampledashboard1.service.RefreshTokenService;
import com.sampledashboard1.utils.MessageUtils;
import com.sampledashboard1.utils.MethodUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final LoginRepository loginRepository;
    private final UsersRepository usersRepository;
    private final LoginService loginService;


    @PostMapping("login")
    public ResponseWrapperDTO login(@RequestBody LoginRequest loginForm, HttpServletRequest httpServletRequest) {

        if(loginForm.getType().equals(EnumForLoginType.EMAIL.value())){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginForm.getEmail());

            Login login = loginRepository.findByEmail(loginForm.getEmail()).orElseThrow(() -> new UserDefineException("user not found"));
            String jwt = jwtProvider.generateToken(userDetails);
            if (Boolean.FALSE.equals(login.getIsActive())) {
                throw new UserDefineException("User not active, Please contact admin");
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(login.getId());

            Users users = usersRepository.getUserByLoginId(login.getId()).orElse(null);
            LoginResponse.LoginResponseBuilder responseBuilder = LoginResponse.builder()
                    .id(login.getId())
                    .token(jwt)
                    .refreshToken(refreshToken.getToken())
                    .email(login.getEmail());

            if (!MethodUtils.isObjectisNullOrEmpty(users)) {
                assert users != null;
                responseBuilder.firstName(users.getFirstName())
                        .userId(users.getId())
                        .lastName(users.getLastName());
            }
            return ResponseWrapperDTO.successResponse(MessageUtils.get("login.controller.login"), responseBuilder.build(), httpServletRequest);
        } else if(loginForm.getType().equals(EnumForLoginType.Google.value())){

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginForm.getEmail());

            Login login = loginRepository.findByEmail(loginForm.getEmail()).orElseThrow(() -> new UserDefineException("user not found"));
            if(loginForm.getGoogleId() != null &&  !loginForm.getGoogleId().isEmpty()) {
                Login login1 = loginRepository.findByGoogleId(loginForm.getGoogleId());
                if (login1 == null) {
                    login.setGoogleId(loginForm.getGoogleId());
                    loginRepository.save(login);
                }
            }
            String jwt = jwtProvider.generateToken(userDetails);
            if (Boolean.FALSE.equals(login.getIsActive())) {
                throw new UserDefineException("User not active, Please contact admin");
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(login.getId());

            Users users = usersRepository.getUserByLoginId(login.getId()).orElse(null);
            LoginResponse.LoginResponseBuilder responseBuilder = LoginResponse.builder()
                    .id(login.getId())
                    .token(jwt)
                    .refreshToken(refreshToken.getToken())
                    .email(login.getEmail());

            if (!MethodUtils.isObjectisNullOrEmpty(users)) {
                assert users != null;
                responseBuilder.firstName(users.getFirstName())
                        .userId(users.getId())
                        .lastName(users.getLastName());
            }
            return ResponseWrapperDTO.successResponse(MessageUtils.get("login.controller.login"), responseBuilder.build(), httpServletRequest);

        }
        return null;
    }

    @PostMapping("refreshToken")
    public ResponseWrapperDTO refreshToken(@Valid @RequestBody TokenRefreshRequest request, HttpServletRequest httpServletRequest) {

        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getLogin)
                .map(login -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(login.getEmail());
                    String token = jwtProvider.generateToken(userDetails);
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(login.getId());
                    return ResponseWrapperDTO.successResponse(MessageUtils.get("login.controller.refreshToken"), TokenRefreshResponse.builder().token(token).refreshToken(newRefreshToken.getToken()).build(), httpServletRequest);
                })
                .orElseThrow(() -> new TokenRefreshException(MessageUtils.get("login.controller.val.refreshToken")));
    }

    /**
     * This API Used for forgot password send OTP in  mail
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping("forgotPwdSendEmail")
    public ResponseWrapperDTO forgotPwdSendEmail(@RequestBody ForgotPassSendOtpRequest request, HttpServletRequest httpServletRequest) {
        String res = loginService.forgotPwdSendEmail(request.getEmail());
        return ResponseWrapperDTO.successResponse(res, res, httpServletRequest);
    }

    /**
     * This API Used for forgot password OTP Verification
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/forgotPwdOtpVerification")
    public ResponseWrapperDTO forgotPwdOtpVerification(@RequestBody ForgotPassSendOtpRequest request, HttpServletRequest httpServletRequest) {
        String res = loginService.forgotPwdOtpVerification(request.getEmail(), request.getOtp());
        return ResponseWrapperDTO.successResponse(res, res, httpServletRequest);
    }

    /**
     * This API Used for forgot Password
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/forgotPwd")
    public ResponseWrapperDTO forgotPwd(@RequestBody ForgotPassRequest forgotPassRequest, HttpServletRequest httpServletRequest) {
        String res = loginService.forgotPwd(forgotPassRequest.getEmail(), forgotPassRequest.getPassword());
        return ResponseWrapperDTO.successResponse(res, res, httpServletRequest);
    }

    /**
     * This API Used for change Password
     * @param request
     * @param httpServletRequest
     *
     */
    @PostMapping("/changePwd")
    public ResponseWrapperDTO changePwd(@Valid @RequestBody ChangePassRequest request, HttpServletRequest httpServletRequest) {
        String res = loginService.changePwd(request.getCrnPass(), request.getPwd());
        return ResponseWrapperDTO.successResponse(res, res, httpServletRequest);
    }

}
