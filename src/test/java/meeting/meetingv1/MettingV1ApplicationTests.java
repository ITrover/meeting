//package meeting.meetingv1;
//
//import jdk.nashorn.internal.ir.annotations.Ignore;
//import meeting.meetingv1.exception.ParameterException;
//import meeting.meetingv1.exception.UnknownAccountException;
//import meeting.meetingv1.mapper.UserMapper;
//import meeting.meetingv1.mapper.UserMeetingMapper;
//import meeting.meetingv1.pojo.User;
//import meeting.meetingv1.pojo.UserMeeting;
//import meeting.meetingv1.service.UserMeetingService;
//import meeting.meetingv1.service.UserService;
//import meeting.meetingv1.service.VerificationCodeService;
//import meeting.meetingv1.util.GenerateVerificationCode;
//import meeting.meetingv1.util.SendSmsUtil;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import com.aliyuncs.CommonRequest;
//import com.aliyuncs.CommonResponse;
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.exceptions.ServerException;
//import com.aliyuncs.http.MethodType;
//import com.aliyuncs.profile.DefaultProfile;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//class MettingV1ApplicationTests {
//
//    @Test@Ignore
//    void contextLoads() {
//    }
//    @Autowired
//    UserMapper userMapper;
//
//
//    @Test@Ignore
//    void match() {
//        String s = "lyujinhao@qq.com";
//        System.out.println(s.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"));
//    }
//
//
//    @Test@Ignore
//    void verification() {
//        System.out.println(GenerateVerificationCode.getVerificationCode_NUM());
//    }
//
//    @Autowired
//    JavaMailSender mailSender;
//    @Test@Ignore
//    void mail() {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("meeting.group@qq.com");
//        mailMessage.setTo("1083775096@qq.com");
//        mailMessage.setSubject("会务管理系统通知");
//        mailMessage.setText("测试");
//        mailSender.send(mailMessage);
//    }
//
//    @Test
//    @Ignore
//    void sendSms() {
//        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FcMvk8PhPNg9uyATK31", "iNJHAFlUNVVy4BbBrWs2xlAS86jgII");
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        CommonRequest request = new CommonRequest();
//        request.setMethod(MethodType.POST);
//        request.setDomain("dysmsapi.aliyuncs.com");
//        request.setVersion("2017-05-25");
//        request.setAction("SendSms");
//        request.putQueryParameter("RegionId", "cn-hangzhou");
//        request.putQueryParameter("PhoneNumbers", "15086924104");
//        request.putQueryParameter("TemplateCode", "SMS_177549030");
//        request.putQueryParameter("SignName", "会务管理系统");
//        request.putQueryParameter("TemplateParam", "{\"code\":\"23212\"}");
//        try {
//            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//    }
//    @Autowired
//    VerificationCodeService verificationCodeService;
//    @Test
//    void verifiedTest() {
//        System.out.println(verificationCodeService.getVerificationCode("15086924104"));
//    }
//    @Autowired
//    SendSmsUtil sendSmsUtil;
//    private Logger log = LoggerFactory.getLogger(this.getClass());
//    @Test@Ignore
//    void verifiedTes2t() {
////        System.out.println(sendSmsUtil.getAccessKeyId());
//        log.info("输出日志");
//        //rSystem.out.println(verificationCodeService.deleteCode("15086924104"));
//    }
//    @Autowired
//    UserService userService;
//    @Test@Ignore
//    void userUpdate() throws UnknownAccountException {
//        userService.updatePwd("15086924104","密码");
//    }
//    @Autowired
//    UserMeetingService userMeetingService;
//    @Test@Ignore
//    void userMeeting() throws ParameterException {
//        List<UserMeeting> list = new ArrayList<>();
//        list.add(new UserMeeting(null,1,2,new Byte("3")));
//        list.add(new UserMeeting(null,1,3,new Byte("3")));
//        list.add(new UserMeeting(null,1,4,new Byte("3")));
//        list.add(new UserMeeting(null,1,5,new Byte("3")));
//        list.add(new UserMeeting(null,1,6,new Byte("3")));
//        for (UserMeeting userMeeting : list){
//            userMeetingService.addRelation(userMeeting);
//        }
//
//    }
//}
