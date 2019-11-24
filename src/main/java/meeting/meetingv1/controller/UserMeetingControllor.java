package meeting.meetingv1.controller;

import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.util.ResultBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMeetingControllor {



    @UserLoginToken
    @PostMapping("/preference/{type}")
    public ResultBean preference(@PathVariable("type") int type, int UserId, int MeetingId){
        if (type != 2 || type != 3){
            return ResultBean.error(-9,"类型错误");
        }

        return ResultBean.success();
    }
}
