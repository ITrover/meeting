package meeting.meetingv1.service;

import meeting.meetingv1.exception.VerificationException;
import meeting.meetingv1.util.GenerateVerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
public class VerificationCodeService {

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
     *验证注册码，如果成功删除缓存
     * @param verificationCode
     * @return
     */
    public boolean doVerified(String mailAddr_or_Phone, String verificationCode) throws VerificationException {
        String code = stringRedisTemplate.opsForValue().get(mailAddr_or_Phone);
        if (code == null || !code.equals(verificationCode)){
            deleteCode(mailAddr_or_Phone);
            throw new VerificationException();
        }
        return true;
    }

    /**
     * 删除指定的key
     * @param mailAddr_or_Phone
     * @return
     */
    private boolean deleteCode(String mailAddr_or_Phone){
        boolean flag = stringRedisTemplate.delete(mailAddr_or_Phone);
        return flag;
    }

}
