package com.sampledashboard1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sampledashboard1.config.audit.Auditable;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = VariableUtils.TABLE_NAME_FOR_CAPTCHA_VERIFICATION)
public class Captcha extends Auditable<Long> {

    private String hiddenCaptcha;
    private String uuid;
    private LocalDateTime expiryTimestamp;
    private Boolean isVerified;

   /* @Transient
    private String captcha;*/

    @Transient
    private String realCaptcha;

    public Captcha( String uuid, String realCaptcha) {
        this.uuid=uuid;
        this.realCaptcha=realCaptcha;
    }
}
