package com.sampledashboard1.service;

import com.sampledashboard1.model.OtpVerification;
import com.sampledashboard1.payload.request.SaveUsersRequest;
import com.sampledashboard1.payload.request.SendOtpRequest;

public interface OtpVerificationService {

     OtpVerification sendOtp(SaveUsersRequest request);

}
