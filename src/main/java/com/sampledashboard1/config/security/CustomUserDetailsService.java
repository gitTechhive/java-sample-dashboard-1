package com.sampledashboard1.config.security;

import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.Login;
import com.sampledashboard1.repository.LoginRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;

    @Override
    @Transactional
    public UserDetails   loadUserByUsername(String username) {
        Login login = loginRepository.findByEmail(username).orElseThrow(() -> new UserDefineException("User not found."));
        if (Boolean.FALSE.equals(login.getIsActive())) {
            throw new UserDefineException("User not active, Please contact admin.");
        }
        return CustomUser.build(login);
    }

    @Transactional
    public UserDetails loadUserById(String id,HttpServletResponse response) throws IOException {
        Login login = loginRepository.getByLoginId(Long.valueOf(id)).orElseThrow(() -> new UserDefineException("User not found."));
        if (Boolean.FALSE.equals(login.getIsActive())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "user not Active");
          //  throw new AccessDeniedException("User not active, Please contact admin.");
        }
        return CustomUser.build(login);
    }


}
