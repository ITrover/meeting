package meeting.meetingv1.controller;

import meeting.meetingv1.annotation.PassToken;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.IncorrectCredentialsException;
import meeting.meetingv1.exception.SignUpColumnException;
import meeting.meetingv1.exception.UnknownAccountException;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.service.UserService;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PassToken
    @RequestMapping("hello")
    public User userController(){
        return userService.findUserById(2);
    }

    @RequestMapping("login")
    public ResultBean login(String key,String password) throws IncorrectCredentialsException, UnknownAccountException {
        Map<String,String> map = new HashMap<>();
        map.put("jwt",userService.loginCheck(key,password));
        return ResultBean.success(map);
    }

    @RequestMapping("signup")
    public ResultBean signUp(User user) throws SignUpColumnException {
        userService.signUp(user);
        return ResultBean.success();
    }
}
