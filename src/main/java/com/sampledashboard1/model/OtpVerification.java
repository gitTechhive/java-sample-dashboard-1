package com.sampledashboard1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sampledashboard1.config.audit.Auditable;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = VariableUtils.TABLE_NAME_FOR_OTP_VERIFICATION)
public class OtpVerification extends Auditable<Long> {
    /**
     * table columns
     */
    private String requestId;
    private String requestType;
    private String requestValue;
    private String otp;

    public OtpVerification(String requestId) {

        this.requestId=requestId;
    }
}
