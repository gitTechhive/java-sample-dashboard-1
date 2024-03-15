package com.sampledashboard1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.filter.ResponseWrapperDTO;
import com.sampledashboard1.model.UserDoc;
import com.sampledashboard1.model.Users;
import com.sampledashboard1.payload.request.MailRequest;
import com.sampledashboard1.payload.request.SaveUsersRequest;
import com.sampledashboard1.payload.request.SignUpGoogleRequest;
import com.sampledashboard1.repository.UserDocsRepository;
import com.sampledashboard1.repository.UsersRepository;
import com.sampledashboard1.service.EmailService;
import com.sampledashboard1.service.OtpVerificationService;
import com.sampledashboard1.service.UsersService;
import com.sampledashboard1.utils.MessageUtils;
import com.sampledashboard1.utils.MethodUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UsersController {
    private static final String UPLOAD_DIR = "C:/app_doc/pic";
    private final UsersService usersService;
    private final EmailService emailService;
    private final OtpVerificationService otpVerificationService;
    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final UserDocsRepository userDocsRepository;

    /**
     * This API Used For OTP verification if(verification is done then after save User)
     *
     * @param saveUsersData
     * @param httpServletRequest
     */
    @Operation( description = "This API Used For OTP verification if(verification is done then after save User)")
    @PostMapping("/")
    public ResponseWrapperDTO otpVerification(@Valid @RequestBody SaveUsersRequest saveUsersData,
                                              HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.save"), usersService.otpVerification(saveUsersData), httpServletRequest);
    }

    /**
     * This API Used For SingUp in Google
     * @param request
     * @param httpServletRequest
     * @return
     */
    @Operation( description = "This API Used For SingUp in Google")
    @PostMapping("/singUpGoogle")
    public ResponseWrapperDTO singUpGoogle(@Valid @RequestBody SignUpGoogleRequest request,
                                           HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.save"), usersService.signUpGoogle(request), httpServletRequest);
    }

    /**
     * This API Used For send email
     *
     * @param mailRequest
     * @param httpServletRequest
     */
    @Operation( description = "This API Used For send email")
    @PostMapping("send")
    public ResponseWrapperDTO sendEmail(@RequestBody MailRequest mailRequest, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse("Send Email Successfully", emailService.sendEmail(mailRequest), httpServletRequest);
    }

    /**
     * This API Used For send OTP in email verification
     *
     * @param sendOtpRequest
     * @param httpServletRequest
     */
    @Operation( description = "This API Used For send OTP in email verification")
    @PostMapping("/sendOtp")
    public ResponseWrapperDTO sendOtp(@RequestBody SaveUsersRequest sendOtpRequest, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse("OTP Send Successfully", otpVerificationService.sendOtp(sendOtpRequest), httpServletRequest);
    }

    /**
     * This API used for update User
     * @param file
     * @param saveUsersData
     * @param httpServletRequest
     */
    @Operation( description = "This API used for update User")
    @PostMapping("/updateUser")
    public ResponseWrapperDTO updateUsers(@Valid @RequestParam(name = "profilePic", required = false) MultipartFile file,
                                          @RequestParam(name = "userInfo") String saveUsersData,
                                          HttpServletRequest httpServletRequest) throws IOException {

        SaveUsersRequest saveUsersRequest = convertJsonToUserRequest(saveUsersData);
        Long userIdByToken = Long.valueOf(MethodUtils.getCurrentUserId());
        Users users = usersRepository.findById(userIdByToken)
                .orElseThrow(() -> new UserDefineException("User not found"));
        UserDoc userDoc = userDocsRepository.findByUserId(userIdByToken);

        if (file != null && !file.isEmpty()) {
           this.handleFile(file, userIdByToken, userDoc);
        } else if (userDoc != null && saveUsersRequest.getProfilePicUrl() == null) {
            usersService.deleteUserDoc(userIdByToken);
        }

        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.update"),
                usersService.updateUsers(saveUsersRequest), httpServletRequest);
    }

    private void handleFile(MultipartFile file, Long userIdByToken, UserDoc userDoc) throws IOException {
        if (userDoc != null) {
            usersService.deleteUserDoc(userIdByToken);
        }

        String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
        File uploadedFile = new File(filePath);
        file.transferTo(uploadedFile);

        if (userDoc == null) {
            userDoc = new UserDoc();
        }
        userDoc.setUrl(filePath);
        userDoc.setOriginalName(file.getOriginalFilename());
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        String s = formattedDateTime + "_" + MethodUtils.generateRandomStringOnlyAlphabet(5) + "." + fileExtension;
        userDoc.setFormattedName(s);
        userDoc.setUser(usersRepository.getOne(userIdByToken));
        userDocsRepository.save(userDoc);
    }

    /**
     * this API used for specific user Id to get data
     * @param httpServletRequest
     * @return user Object
     */
    @Operation( description = "This API used for specific user Id to get data")
    @GetMapping("/getUserData")
    public ResponseWrapperDTO getUserData( HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.profile"), usersService.getUsers(), httpServletRequest);
    }

    @GetMapping("/hello")
    public String welcome() {
        return "java-sample-dashboard-1 ";
    }
    private SaveUsersRequest convertJsonToUserRequest(String user) {
        SaveUsersRequest saveUsersRequest;
        try {
            saveUsersRequest = objectMapper.readValue(user, SaveUsersRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return saveUsersRequest;
    }
}
