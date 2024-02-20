package com.sampledashboard1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sampledashboard1.config.audit.Auditable;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

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
    @Column(name="pinCode")
    private String pinCode;
    @Column(name = "mobileNo")
    private Long mobileNo;
    private String bio;
    private String type;
    private String phoneCode;


    /**
     * relation mapping
     */
    @OneToOne
    @JsonIgnore
    private Login login;

    @ManyToOne
    @JsonIgnore
    private Countries country;

    @ManyToOne
    @JsonIgnore
    private States state;

    @ManyToOne
    @JsonIgnore
    private Cities cities;

    /***
     *  others
     */
    @Transient
    private Map<String, Object> otherData;
    @Transient
    private String country_id; ;
    @Transient
    private String state_id;
    @Transient
    private String cities_id;
    @Transient
    private String url;
    @Transient
    private String email;
    @Transient
    private String profilePicUrl;

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
    //UsersRepository :getUsersData
    public Users(Long id,String firstName, String lastName,String address,String pinCode,Long mobileNo,String bio,
                 String type,String phoneCode,String country_id ,String states,String citie,String email,String urls) {
        this.id = id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.address=address;
        this.pinCode=pinCode;
        this.mobileNo=mobileNo;
        this.bio=bio;
        this.type=type;
        this.phoneCode=phoneCode;
        this.country_id=country_id;
        this.state_id=states;
        this.cities_id=citie;
        this.email=email;
        this.profilePicUrl=urls;

    }
}
 /*  HashMap<String, Object> data = new HashMap<>();
        if (!MethodUtils.isObjectisNullOrEmpty(countrise)) {
            data.put("countrise", countrise);
            this.otherData = data;
        }*/