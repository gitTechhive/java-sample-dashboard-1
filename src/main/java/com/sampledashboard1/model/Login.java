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
@Table(name = VariableUtils.TABLE_NAME_FOR_LOGIN)
public class Login  extends Auditable<Long> {
    /**
     * table columns
     */
    private String email;
    private String password;
    private String googleId;

    /**
     * Other details
     */
    @Transient
    private Users users;

    // LoginRepository:getByLoginId
    public Login(Long id, String email, String password, Boolean isActive, String firstName, Long userId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.users = new Users(userId,firstName);

    }
}
