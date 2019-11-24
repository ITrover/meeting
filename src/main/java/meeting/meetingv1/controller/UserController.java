package meeting.meetingv1.controller;

import com.aliyuncs.exceptions.ClientException;
import meeting.meetingv1.annotation.PassToken;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.IncorrectCredentialsException;
import meeting.meetingv1.exception.SignUpColumnException;
import meeting.meetingv1.exception.UnknownAccountException;
import meeting.meetingv1.exception.VerificationException;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.service.MailService;
import meeting.meetingv1.service.SmsService;
import meeting.meetingv1.service.UserService;
import meeting.meetingv1.service.VerificationCodeService;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SmsService smsService;
    @Autowired
    VerificationCodeService codeService;
    @Autowired
    MailService mailService;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @PassToken
    @RequestMapping("hello")
    public User userController(){
        return userService.findUserById(2);
    }

    @RequestMapping("login")
    public ResultBean login(String key,String password) throws IncorrectCredentialsException, UnknownAccountException {
        Map<String,Object> map = new HashMap<>();
        map.put("jwt",userService.loginCheck(key,password));
        map.put("user",userService.findUserByKey(key));
        return ResultBean.success(map);
    }

    /**
     * 注册
     * @param user
     * @return
     * @throws SignUpColumnException
     */
    @PostMapping("signup")
    public ResultBean signUp(User user,String verificationCode) throws SignUpColumnException, VerificationException {
        codeService.doVerified(user.getPhone(),verificationCode);
        userService.signUp(user);
        log.info("用户："+user.getUsername() + " 成功注册");
        return ResultBean.success();
    }

    /**
     * 获取验证码
     * @param mailAddr_or_Phone
     * @return
     * @throws ClientException
     */
    @PostMapping("verificationCode")
    public ResultBean getVriCode(String mailAddr_or_Phone) throws ClientException {
        boolean isMail = mailAddr_or_Phone.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        if (isMail){
            mailService.SendToMail(mailAddr_or_Phone,"您好，您的验证码为 " + codeService.getVerificationCode(mailAddr_or_Phone));
        }else {
            smsService.sendVriCode(codeService.getVerificationCode(mailAddr_or_Phone),mailAddr_or_Phone);
        }
        return ResultBean.success();
    }

    @PostMapping("updatePwd")
    public ResultBean updatePassword(String mailAddr_or_Phone, String password, String verificationCode ) throws VerificationException, UnknownAccountException {
        codeService.doVerified(mailAddr_or_Phone,verificationCode);
        userService.updatePwd(mailAddr_or_Phone, password);
        return ResultBean.success();
    }
    @UserLoginToken
    @PostMapping("update")
    public ResultBean updateUserInfo(User user){
        userService.updateUserInfo(user);
        return ResultBean.success();
    }
}
