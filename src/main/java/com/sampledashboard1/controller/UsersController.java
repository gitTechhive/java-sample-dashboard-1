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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;


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
    @PostMapping("send")
    public ResponseWrapperDTO sendEmail(@RequestBody MailRequest mailRequest, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse("hello send done", emailService.sendEmail(mailRequest), httpServletRequest);
    }

    /**
     * This API Used For send OTP in email verification
     *
     * @param sendOtpRequest
     * @param httpServletRequest
     */

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
    @PostMapping("/updateUser")
    public ResponseWrapperDTO updateUsers(@Valid @RequestParam(name = "profilePic", required = false) MultipartFile file
            , @RequestParam(name = "userInfo") String saveUsersData,
                                          HttpServletRequest httpServletRequest) throws IOException {
        Long userIdByToken = Long.valueOf(MethodUtils.getCurrentUserId());
        Users users = usersRepository.findById(userIdByToken).orElseThrow(() -> new UserDefineException("User not found"));
        UserDoc userDoc = null;
        userDoc = userDocsRepository.findByUserId(userIdByToken);
        if((file == null || file.isEmpty()) && userDoc != null){
            usersService.deleteUserDoc(userIdByToken);
        }else{
            byte[] bytes = file.getBytes();
            String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
            File uploadedFile = new File(filePath);
            file.transferTo(uploadedFile);

            if (userDoc == null) {
                userDoc = new UserDoc();
            }
            userDoc.setUrl(filePath);
            userDoc.setOriginalName(file.getOriginalFilename());
            LocalDateTime currentDateTime = LocalDateTime.now();
            String year = String.valueOf(currentDateTime.getYear());
            String month = String.valueOf(currentDateTime.getMonthValue());
            String day = String.valueOf(currentDateTime.getDayOfMonth());
            String hour = String.valueOf(currentDateTime.getHour());
            String minute = String.valueOf(currentDateTime.getMinute());
            String second = String.valueOf(currentDateTime.getSecond());

            String fileExtension = "";
            int dotIndex = file.getOriginalFilename().lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < file.getOriginalFilename().length() - 1) {
                fileExtension = file.getOriginalFilename().substring(dotIndex + 1);
            }
            String s = year+month+day + "_" + hour + minute + second + "_" + MethodUtils.generateRandomStringOnlyAlphabet(5) + "." + fileExtension;
            userDoc.setFormattedName(s);
            userDoc.setUser(users);
            userDocsRepository.save(userDoc);
        }
        SaveUsersRequest saveUsersRequest = convertJsonToUserRequest(saveUsersData);
        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.update"), usersService.updateUsers(saveUsersRequest), httpServletRequest);
    }

    /**
     * this API used for specific user Id to get data
     * @param httpServletRequest
     * @return user Object
     */

    @GetMapping("/getUserData")
    public ResponseWrapperDTO getUserData( HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("users.controller.profile"), usersService.getUsers(), httpServletRequest);
    }

    @GetMapping("/hello")
    public String welcome() {
        return "java-sample-dashboard-1 ";
    }

    // string data convert SaveUsersRequest class
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
