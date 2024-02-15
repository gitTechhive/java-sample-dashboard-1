package com.sampledashboard1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sampledashboard1.config.audit.Auditable;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = VariableUtils.TABLE_NAME_FOR_USER)
public class Users  extends Auditable<Long> {

    /**
     * Table Column
     */
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    private String address;
    private String country;
    private String state;
    @Column(name="pinCode")
    private String pinCode;
    @Column(name = "mobileNo")
    private Long mobileNo;


    /**
     * relation mapping
     */
    @OneToOne
    @JsonIgnore
    private Login login;
    public Users(Long id) {
        this.id = id;
    }

    public Users(Long id,String firstName) {
        this.id = id;
        this.firstName=firstName;
    }
    public Users(Long id,String firstName,String lastName) {
        this.id = id;
        this.firstName=firstName;
        this.lastName=lastName;
    }
}
