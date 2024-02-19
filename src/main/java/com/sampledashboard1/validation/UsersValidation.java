package com.sampledashboard1.validation;

import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.Login;
import com.sampledashboard1.model.Users;
import com.sampledashboard1.payload.request.SaveUsersRequest;
import com.sampledashboard1.repository.LoginRepository;
import com.sampledashboard1.repository.UsersRepository;
import com.sampledashboard1.utils.MethodUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsersValidation {

    private final UsersRepository usersRepository;
    private final LoginRepository loginRepository;

    public void checkMobileNoIsExits(Long mobileNo, Long userId) {
        List<Users> usersList = usersRepository.getByMobileNoAndId(mobileNo, userId);
        if (!MethodUtils.isListIsNullOrEmpty(usersList)) {
            throw new UserDefineException("Mobile Number Already Exist.");
        }
    }

    public void checkEmailIsExits(String email, Long userId) {
        List<Login> loginList = loginRepository.getByEmailAndUserId(email, userId);
        if (!MethodUtils.isListIsNullOrEmpty(loginList)) {
            throw new UserDefineException("Email Already Exist.");
        }
    }

    public void checkUserUpdateValidation(SaveUsersRequest request, Long userId) {
        checkEmailIsExits(request.getEmail(), userId);
        checkMobileNoIsExits(request.getMobileNo(), userId);
    }
}
