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
            where o.requestId=:requestId
            """)
    OtpVerification getDataByEmailOrOtpAndUId(String requestId);

   OtpVerification findByRequestValue(String email);


}