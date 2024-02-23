package com.sampledashboard1.config.audit;

import com.sampledashboard1.utils.MethodUtils;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Log4j2
public class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

    @Nonnull
    @Override
    public Optional<Long> getCurrentAuditor() {
        String currentLoginId = MethodUtils.getCurrentUserId();
        return Optional.of(MethodUtils.isObjectisNullOrEmpty(currentLoginId) ? 0L : Long.parseLong(currentLoginId));
    }
}
