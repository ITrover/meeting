package meeting.meetingv1.controller;

import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.Message;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.service.MsgService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MsgController {

    @Autowired
    MsgService msgService;

    @GetMapping("getMessage")
    @UserLoginToken
    @ApiOperation(value = "获取用户收到的信息",notes = "参数： 1、登陆Token")
    public ResultBean getUserById(HttpServletRequest request){
        Map<String,List<Message>> map = new HashMap<>();
        map.put("messages",msgService.getMsgByUserID(Check.getUserID(request)));
        return ResultBean.success(map);
    }
}
