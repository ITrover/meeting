package meeting.meetingv1.service;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import meeting.meetingv1.util.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Autowired
    SendSmsUtil smsUtil;

    /**
     * 发送验证码
     */
    public boolean sendVriCode(String verificationCode, String phoneNumber) throws ServerException, ClientException {
        return smsUtil.sendSms(verificationCode,phoneNumber);
    }
}
