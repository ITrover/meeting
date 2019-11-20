package meeting.meetingv1;

import meeting.meetingv1.mapper.UserMapper;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.util.GenerateVerificationCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

@SpringBootTest
class MettingV1ApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    UserMapper userMapper;
    @Test
    void teat1() {
        User user = new User();
//        user.setCreattime(new Long(System.currentTimeMillis()).toString());
//        user.setGroups("重庆邮电大学");
//        user.setMail("1083775096@qq.com");
//        user.setName("吕进豪");
//        user.
        user.setEmailaddr("1083775096@qq.com");
        user.setUsername("lvlvlvlv");
        user.setPhone("15023654968");
        user.setGender("男");
        user.setPassword("123456");
        System.out.println(userMapper.insert(user));

    }

    @Test
    void match() {
        String s = "lyujinhao@qq.com";
        System.out.println(s.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"));
    }


    @Test
    void verification() {
        System.out.println(GenerateVerificationCode.getVerificationCode_NUM());
    }

    @Autowired
    JavaMailSender mailSender;
    @Test
    void mail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("meeting.group@qq.com");
        mailMessage.setTo("1083775096@qq.com");
        mailMessage.setSubject("会务管理系统通知");
        mailMessage.setText("哈哈哈哈哈哈哈哈啊哈哈");
        mailSender.send(mailMessage);
    }

    @Test
    void sendSms() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FeKTXuVcsmA77uScp95", "xvVZUn3UPYwuoomoKH4fJVz9vNLdQx");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", "15086924104");
        request.putQueryParameter("TemplateCode", "SMS_177549030");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
