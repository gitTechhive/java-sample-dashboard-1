package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.exception.TokenRefreshException;
import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.Login;
import com.sampledashboard1.model.RefreshToken;
import com.sampledashboard1.repository.LoginRepository;
import com.sampledashboard1.repository.RefreshTokenRepository;
import com.sampledashboard1.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${dng.app.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;
    private final LoginRepository loginRepository;

    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long loginId) {
        RefreshToken refreshToken = new RefreshToken();

        Login login = loginRepository.findById(loginId).orElseThrow(()-> new UserDefineException("login not found."));
        refreshToken.setLogin(login);
       // refreshToken.setLoginId(loginId);
        refreshToken.setExpiryDate(LocalDateTime.now().plus(refreshTokenDurationMs, ChronoUnit.MILLIS));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }
}
