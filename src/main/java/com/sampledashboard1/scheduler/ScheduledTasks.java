package com.sampledashboard1.scheduler;

import com.sampledashboard1.service.CaptchaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Log4j2
@RequiredArgsConstructor
public class ScheduledTasks {

    private final CaptchaService captchaService;


    @Scheduled(cron = "0 5 0 1/1 * *")
    @Transactional
    public void dailyScheduler() {
        LocalDate date = LocalDate.now();
        log.info("Daily scheduler called at {}", date);
        captchaService.deleteCaptcha(date.minusDays(7));
    }

}
