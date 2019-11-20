package meeting.meetingv1.service;

import meeting.meetingv1.util.GenerateVerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class VirificationCodeService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;//k-v 都是字符串
    /**
     * 获取验证码并存入缓存，
     * @param mailAddr_or_Phone
     * @return
     */
    public String getVerificationCode(String mailAddr_or_Phone){
        String code = GenerateVerificationCode.getVerificationCode_NUM();
        stringRedisTemplate.opsForValue().set(mailAddr_or_Phone,code,600, TimeUnit.SECONDS);
        return code;
    }

    /**
     *
     * @param verificationCode
     * @return
     */
    public boolean doVerified(String verificationCode){
        return false;
    }

}
