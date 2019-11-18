package meeting.meetingv1.controller;

import meeting.meetingv1.annotation.PassToken;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PassToken
    @RequestMapping("hello")
    public User userController(){
        return userService.findUserById(2);
    }
}
