package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.model.Captcha;
import com.sampledashboard1.model.Login;
import com.sampledashboard1.repository.CaptchaRepository;
import com.sampledashboard1.service.CaptchaService;
import com.sampledashboard1.utils.CaptchaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final CaptchaRepository captchaRepository;

    @Override
    public Captcha generateCaptcha() {

        Captcha  newCaptcha=new Captcha();
        cn.apiclub.captcha.Captcha captcha = CaptchaUtil.createCaptcha(240, 70);
        newCaptcha.setHiddenCaptcha(captcha.getAnswer());
      //  newCaptcha.setCaptcha("");
        String s="data:image/png;base64,";
        newCaptcha.setRealCaptcha(s+CaptchaUtil.encodeCaptcha(captcha));
        String string = UUID.randomUUID().toString();
        newCaptcha.setUuid(string);
        LocalDateTime currentDateTime = LocalDateTime.now();
        newCaptcha.setExpiryTimestamp(currentDateTime.plusMinutes(1));
        captchaRepository.save(newCaptcha);
        return new Captcha(newCaptcha.getUuid(),newCaptcha.getRealCaptcha()) ;
    }

    @Override
    public Captcha reGenerateCaptcha(String uuid) {
        Captcha dataByUID = captchaRepository.getDataByUID(uuid);

        if(dataByUID != null){
            Captcha  newCaptcha=dataByUID;
            cn.apiclub.captcha.Captcha captcha = CaptchaUtil.createCaptcha(240, 70);
            newCaptcha.setHiddenCaptcha(captcha.getAnswer());
            String s="data:image/png;base64,";
            newCaptcha.setRealCaptcha(s+CaptchaUtil.encodeCaptcha(captcha));
            String string = UUID.randomUUID().toString();
            newCaptcha.setUuid(string);
            LocalDateTime currentDateTime = LocalDateTime.now();
            newCaptcha.setExpiryTimestamp(currentDateTime.plusMinutes(1));
            captchaRepository.save(newCaptcha);
            return new Captcha(newCaptcha.getUuid(),newCaptcha.getRealCaptcha());
        }else{
            throw new UserDefineException("UUId Not found !");
        }
    }

    @Override
    public String verificationCaptcha(String uuid, String hiddenCaptcha) {
         Captcha captcha = captchaRepository.getDataByUIdAndHiddenCaptcha(uuid,hiddenCaptcha);
        if(captcha != null){
            LocalDateTime currentDateTime = LocalDateTime.now();
            int i = captcha.getExpiryTimestamp().compareTo(currentDateTime);
            if(i<0){
                throw new UserDefineException("Your Captcha Expire.");
            }
            captcha.setIsVerified(true);
           // captcha.setExpiryTimestamp(currentDateTime.plusMinutes(1));
            captchaRepository.save(captcha);
        }
       else{
           throw new UserDefineException("Please Valid Captcha");
        }
        return "Captcha Verification Successfully";
    }

    @Override
    @Modifying
    public String deleteCaptcha(LocalDate date) {
        captchaRepository.deleteCaptcha(date);
        return "Delete Successfully";
    }
}
