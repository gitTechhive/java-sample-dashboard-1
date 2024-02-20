package com.sampledashboard1.repository;

import com.sampledashboard1.model.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {

    @Query("select c from Captcha as c where c.uuid=:uuid")
    Captcha getDataByUID(String uuid);

    @Query("select c from Captcha as c where c.uuid=:uuid and c.hiddenCaptcha=:hiddenCaptcha")
    Captcha getDataByUIdAndHiddenCaptcha(String uuid, String hiddenCaptcha);

    @Modifying
    @Query("""
            delete from Captcha c where extract(date from :date)=extract(date from c.expiryTimestamp)
            """)
    Object deleteCaptcha(LocalDate date);
}
