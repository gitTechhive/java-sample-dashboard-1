package com.sampledashboard1.repository;

import com.sampledashboard1.model.Login;
import com.sampledashboard1.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    @Query("""
            select o from OtpVerification o 
            where o.requestId=:requestId and o.otp=:otp
            """)
    OtpVerification getDataByUId(String requestId,String otp);

   OtpVerification findByRequestValue(String email);
    @Query("""
            select o from OtpVerification o 
            where o.requestValue=:email and o.otp=:otp and o.requestId=:requestId
            """)
    OtpVerification getDataByEmailOrOtp(String email,String otp,String requestId);
    @Query("""
            select o from OtpVerification o 
            where o.requestValue=:phoneNo and o.otp=:otp 
            """)
    OtpVerification getDataByPhoneNoOrOtp(String phoneNo,String otp);
    @Query("""
            select o from OtpVerification o 
            where o.requestValue=:phoneNo 
            """)
    OtpVerification getDataByPhoneNo(String phoneNo);


}
