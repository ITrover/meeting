package meeting.meetingv1.controller;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.exception.IncorrectCredentialsException;
import meeting.meetingv1.exception.SignUpColumnException;
import meeting.meetingv1.exception.UnknownAccountException;
import meeting.meetingv1.exception.VerificationException;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.service.*;
import meeting.meetingv1.util.JsonUtil;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@Api(tags = "用户信息处理接口")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SmsService smsService;
    @Autowired
    VerificationCodeService codeService;
    @Autowired
    MailService mailService;
    @Autowired
    JsonUtil jsonUtil;

    @Autowired
    MeetingService meetingService;
    private Logger log = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    KafkaSender kafkaSender;
    @PostMapping("hello/{s}")//测试用
    public String userController(@PathVariable String s,@RequestBody String json){
//        kafkaSender.sendMsg("test2","121","测试消息1111111 "+s);
        System.out.println(json);
        System.out.println("发送完成");
        return "";
    }

    @PostMapping("publicInfo")
    @ApiOperation(value = "获取用户公开的信息",notes = "参数： 1、用户ID <br>说明：这个接口的主要目的在会议的组织者查看请求的志愿者信息，其他用途也可以，并没设置登陆验证")
    public ResultBean getUserById(Integer userId){
        User userById = userService.findUserById(userId);
        userById.setPhone(null);
        userById.setPassword(null);
//        userById.setUserid(null);
        Map<String, User> map = new HashMap<>();
        map.put("info",userById);
        return ResultBean.success(map);
    }

    @PostMapping("login")
    @ApiOperation(value = "登陆获取token",notes = "参数： 1、手机或邮箱：key  2、密码 password<br>返回：json中data字段有两个，jwt的值为token字符串，user为用户信息的序列化")
    public ResultBean login(String key, String password) throws IncorrectCredentialsException, UnknownAccountException {
        Map<String,Object> map = new HashMap<>();
        map.put("jwt",userService.loginCheck(key,password));
        map.put("user",userService.findUserByKey(key));
        return ResultBean.success(map);
    }
    @GetMapping("info")
//    @UserLoginToken
    @ApiOperation(value = "获取当前登陆用户信息",notes = "参数： 1、登陆token<br>返回info字段为用户信息的json字符串")
    public ResultBean getUserInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        Map<String, User> map = new HashMap<String, User>();
        map.put("info",userService.findUserById(userId));
        return ResultBean.success(map);
    }

    /**
     * 注册
     * @param userJson
     * @return 22049
     * @throws SignUpColumnException
     */
    @PostMapping("signup")
    @ApiOperation(value = "用户注册",notes = "参数： " +
            "<br>1、验证码 verificationCode; " +
            "<br>2、用户信息 userJson 其中包括：realname、username(必选)、gender、emailaddr(必选)、phone(必选)、password(必选)、organization," +
            "<br>以上信息为对象序列化后得Json串使用UTF-8编码后得字符串")
    public ResultBean signUp(String userJson, String verificationCode) throws SignUpColumnException, VerificationException, UnsupportedEncodingException, JsonProcessingException {
        User user = (User)jsonUtil.decodeUTF8JsonToObject(userJson, User.class);
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
    @ApiOperation(value = "获取验证码",notes = "参数： 1、手机或邮箱：mailAddr_or_Phone<br>注意：验证码可用于注册和修改用户信息")
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
    @ApiOperation(value = "修改密码",notes = "参数： 1、手机或邮箱：mailAddr_or_Phone 2、密码 password 3、验证码verificationCode")
    public ResultBean updatePassword(String mailAddr_or_Phone, String password, String verificationCode ) throws VerificationException, UnknownAccountException {
        codeService.doVerified(mailAddr_or_Phone,verificationCode);
        userService.updatePwd(mailAddr_or_Phone, password);
        return ResultBean.success();
    }
//    @UserLoginToken
    @PostMapping("update")
    @ApiOperation(value = "修改用户信息",notes = "参数： " +
            "1、登陆token" +
            "<br>2、用户信息 userJson ，使用UTF8编码后的json字符串 " +
            "<br>可选字段：用户名 username;真实名 realname;性别 gender;所属组织 organization;")
    public ResultBean updateUserInfo(String userJson) throws UnsupportedEncodingException, JsonProcessingException {
        User user = (User) jsonUtil.decodeUTF8JsonToObject(userJson, User.class);
        userService.updateUserInfo(user);
        return ResultBean.success();
    }
    @ResponseBody
//    @UserLoginToken
    @GetMapping("getSystemInfo")
    @ApiOperation(value = "返回系统信息")
    public ResultBean systemInfo(){
        List list = new ArrayList();
        list.add("CQUPT Nexus，系统运行正常");
        return ResultBean.success(list);
    }

    @ResponseBody
    @GetMapping("getMyCollect")
    @ApiOperation(value="返回我的收藏信息")
    public ResultBean MyCollect(Integer userId) {
        //日期向后延一天
        Date current=new Date();
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(current);
        calendar.add(calendar.DATE,1);
        current=calendar.getTime();
        //
        Meeting meeting1=new Meeting();
        meeting1.setIntroduction("会议1");
        meeting1.setLocation("YF107");
        meeting1.setmName("文峰通信论坛");
        meeting1.setSchedule("上午十点");
        meeting1.setNeedvolunteer(2);
        meeting1.setTypeid(99);
        meeting1.setOrganizer("YF107");
        meeting1.setHostedby("校长");
        meeting1.setCommunicate("123456");
        meeting1.setStartTime(new Date());
        meeting1.setCloseTime(current);
        Map<String, Meeting> maps=new HashMap<String, Meeting>();
        maps.put("收藏1",meeting1);

        return ResultBean.success(maps);
    }
}
