package meeting.meetingv1.controller;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    UserService userService;


    SmsService smsService;


    VerificationCodeService codeService;

    MailService mailService;


    JsonUtil jsonUtil;


    MeetingService meetingService;

    @Autowired
    public UserController(UserService userService, SmsService smsService, VerificationCodeService codeService, MailService mailService, JsonUtil jsonUtil, MeetingService meetingService) {
        this.userService = userService;
        this.smsService = smsService;
        this.codeService = codeService;
        this.mailService = mailService;
        this.jsonUtil = jsonUtil;
        this.meetingService = meetingService;
    }

    private Logger log = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    KafkaSender kafkaSender;

    /**
     *
     * @param s 测试
     * @param json  测试
     * @return 测试
     */
    @PostMapping("hello/{s}")//测试用
    public String userController(@PathVariable String s,@RequestBody String json){
//        kafkaSender.sendMsg("test2","121","测试消息1111111 "+s);
        System.out.println(json);
        System.out.println("发送完成");
        return "";
    }

    /**
     * @author NMID
     * @param userId  用户ID
     * @return  用户公开的信息的map
     */
    @PostMapping("publicInfo")
    @ApiOperation(value = "获取用户公开的信息",notes = "参数： 1、用户ID <br>说明：这个接口的主要目的在会议的组织者查看请求的志愿者信息，其他用途也可以，并没设置登陆验证",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value="用户号",required = true,dataType = "Integer"),
    })
    public ResultBean getUserById(Integer userId){
        User userById = userService.findUserById(userId);
        userById.setPhone(null);
        userById.setPassword(null);
        userById.setUserid(null);
        Map<String, User> map = new HashMap<>(100);
        map.put("info",userById);
        return ResultBean.success(map);
    }

    /**
     * @author NMID
     * @param key  登录key
     * @param password  登录密码
     * @return  返回一个map集合包含token字符串和用户信息的序列化
     * @throws IncorrectCredentialsException    密码认证异常
     * @throws UnknownAccountException 不知道账户异常
     */
    @PostMapping("login")
    @ApiOperation(value = "登陆获取token",notes = "参数： 1、手机或邮箱：key  2、密码 password<br>返回：json中data字段有两个，jwt的值为token字符串，user为用户信息的序列化",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password",value="密码",dataType = "String"),
            @ApiImplicitParam(name=  "key",value="登录key",dataType ="String" )
    })
    public ResultBean login(String key, String password) throws IncorrectCredentialsException, UnknownAccountException {
        Map<String,Object> map = new HashMap<>();
        map.put("jwt",userService.loginCheck(key,password));
        map.put("user",userService.findUserByKey(key));
        return ResultBean.success(map);
    }

    /**
     * @author NMID
     * @param request  请求头封装对象
     * @return
     */
    @GetMapping("info")
//    @UserLoginToken
    @ApiOperation(value = "获取当前登陆用户信息",notes = "参数： 1、登陆token<br>返回info字段为用户信息的json字符串",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request",value="请求头封装对象",required = true,dataType = "HttpServletRequest"),
    })
    public ResultBean getUserInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        Map<String, User> map = new HashMap<String, User>(100);
        map.put("info",userService.findUserById(userId));
        return ResultBean.success(map);
    }

    /**
     * 注册
     * @param userJson  用户注册信息
     * @return 22049
     * @throws SignUpColumnException
     */
    @PostMapping("signup")
    @ApiOperation(value = "用户注册",notes = "参数： " +
            "<br>1、验证码 verificationCode; " +
            "<br>2、用户信息 userJson 其中包括：realname、username(必选)、gender、emailaddr(必选)、phone(必选)、password(必选)、organization," +
            "<br>以上信息为对象序列化后得Json串使用UTF-8编码后得字符串",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "verificationCode",value="验证码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "userJson",value="用户信息",required = true,dataType = "String"),
    })
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
     * @return ResultBean.success
     * @throws ClientException
     */
    @PostMapping("verificationCode")
    @ApiOperation(value = "获取验证码",notes = "参数： 1、手机或邮箱：mailAddr_or_Phone<br>注意：验证码可用于注册和修改用户信息",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mailAdd_or_Phone",value="获取验证码的手机或者邮箱",required = true,dataType = "String")
    })
    public ResultBean getVriCode(String mailAddr_or_Phone) throws ClientException {
        boolean isMail = mailAddr_or_Phone.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        if (isMail){
            mailService.SendToMail(mailAddr_or_Phone,"您好，您的验证码为 " + codeService.getVerificationCode(mailAddr_or_Phone));
        }else {
            smsService.sendVriCode(codeService.getVerificationCode(mailAddr_or_Phone),mailAddr_or_Phone);
        }
        return ResultBean.success();
    }

    /**
     * @author NMID
     * @param mailAddr_or_phone 手机或邮箱
     * @param password  用户登录密码
     * @param verificationCode  验证码
     * @return 成功后返回成功状态码和“success”信息
     * @throws VerificationException
     * @throws UnknownAccountException
     */
    @PostMapping("updatePwd")
    @ApiOperation(value = "修改密码",notes = "参数： 1、手机或邮箱：mailAddr_or_Phone 2、密码 password 3、验证码verificationCode",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "verificationCode",value="验证码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value="密码",required = true,dataType = "String"),
            @ApiImplicitParam(name=  "mailAddr_or_Phone",value="登录key",required = true,dataType ="String" )
    })
    public ResultBean updatePassword(String mailAddr_or_phone, String password, String verificationCode ) throws VerificationException, UnknownAccountException {
        codeService.doVerified(mailAddr_or_phone,verificationCode);
        userService.updatePwd(mailAddr_or_phone, password);
        return ResultBean.success();
    }

    /**
     * @author NMID
     * @param userJson 用户信息
     * @return      成功信息
     * @throws UnsupportedEncodingException
     * @throws JsonProcessingException
     */
    @PostMapping("update")
    //    @UserLoginToken
    @ApiOperation(value = "修改用户信息",notes = "参数： " +
            "1、登陆token" +
            "<br>2、用户信息 userJson ，使用UTF8编码后的json字符串 " +
            "<br>可选字段：用户名 username;真实名 realname;性别 gender;所属组织 organization;",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userJson",value="用户信息",required = true,dataType = "String"),
    })
    /**
     *
     */
    public ResultBean updateUserInfo(String userJson) throws UnsupportedEncodingException, JsonProcessingException {
        User user = (User) jsonUtil.decodeUTF8JsonToObject(userJson, User.class);
        userService.updateUserInfo(user);
        return ResultBean.success();
    }

    /**
     * @auther NMID
     * @return
     */
    @ResponseBody
//    @UserLoginToken
    @GetMapping("getSystemInfo")
    @ApiOperation(value = "返回系统信息",response = ResultBean.class)
    /**
     *
     */
    public ResultBean systemInfo(){
        List list = new ArrayList();
        list.add("CQUPT Nexus，系统运行正常");
        return ResultBean.success(list);
    }

    /**
     *
     * @param userId  用户ID
     * @return
     */
    @ResponseBody
    @GetMapping("getMyCollect")
    @ApiOperation(value="返回我的收藏信息",response = ResultBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value="用户ID",required = true,dataType = "Integer"),
    })
    public ResultBean myCollect(Integer userId) {
        //日期向后延一天
        Date current=new Date();
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(current);
        calendar.add(GregorianCalendar.DATE,1);
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
        Map<String, Meeting> maps=new HashMap<String, Meeting>(100);
        maps.put("收藏1",meeting1);

        return ResultBean.success(maps);
    }
}
