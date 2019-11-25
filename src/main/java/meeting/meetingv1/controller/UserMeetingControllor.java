package meeting.meetingv1.controller;

import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserMeetingControllor {

    @Autowired
    UserMeetingService userMeetingService;
    @UserLoginToken
    @PostMapping("/preference/{type}")
    public ResultBean preference(@PathVariable("type") Integer type, int userId, int meetingId) throws ParameterException {
        userMeetingService.addRelation(new UserMeeting(null,userId,meetingId,new Byte(type.toString())));
        return ResultBean.success();
    }

    /***
     * type 1为创建 2为参加 3为收藏
     * 根据用户查询对应的
     * @param type
     * @param userId
     * @return
     */
    @GetMapping("/preference/{type}")
    public ResultBean getPreferenceByUser(@PathVariable("type") String type, int userId){
        Map<String, List> map = new HashMap<>();
        List<UserMeeting> byMeet = userMeetingService.findPreferenceByUser(userId, new Byte(type));
        map.put("data",byMeet);
        return ResultBean.success(map);
    }
    @GetMapping("/meetPreference/{type}")
    public ResultBean getPreference(@PathVariable("type") String type, int meetingId){
        Map<String, List> map = new HashMap<>();
        List<UserMeeting> byMeet = userMeetingService.findPreferenceByMeet(meetingId, new Byte(type));
        map.put("data",byMeet);
        return ResultBean.success(map);
    }
    @GetMapping("/statistics/{type}")
    public ResultBean getCount(@PathVariable("type") String type, int meetingId){
        int count = userMeetingService.getCount(new Byte(type), meetingId);
        Map<String,Integer> map = new HashMap<>();
        map.put("count",count);
        return ResultBean.success(map);
    }
}
