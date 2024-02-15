package com.sampledashboard1.service;

import com.sampledashboard1.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(Long loginId);
    RefreshToken verifyExpiration(RefreshToken token);
}
