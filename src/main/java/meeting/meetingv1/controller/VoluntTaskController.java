package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.pojo.Voluntask;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.service.VoTaskService;
import meeting.meetingv1.service.VoUserTaskInfoService;
import meeting.meetingv1.service.VoluntEventService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "会议志愿工作接口")
public class VoluntTaskController {

    @Autowired
    VoTaskService voTaskService;
    @Autowired
    VoUserTaskInfoService voUserTaskInfoService;
    @Autowired
    UserMeetingService userMeetingService;
    @Autowired
    VoluntEventService voluntEventService;
    @GetMapping("tasks/{meetingId}")
    @ApiOperation(value = "获取用户申请的的志愿工作详情" ,notes = "" +
            "<br>参数：1. 登陆token" +
            "<br>返回数组中每一个元素是对应的志愿申请状态typeFlag、志愿者信息voluntinfo、工作信息myTask，例子：" +
            "[{\"typeFlag\":4,\"voluntinfo\":{\"userid\":17,\"meetingid\":21,\"taskid\":2,\"studentid\":\"2017210403\",\"personid\":\"5222661998\"},\"myTask\":{\"taskid\":2,\"meetid\":21,\"taskinfo\":\"测试是的 的的的的\",\"workingtime\":8,\"numbers\":2}}," +
            "<br>{\"typeFlag\":4,\"voluntinfo\":{\"userid\":17,\"meetingid\":24,\"taskid\":8,\"studentid\":\"123123\",\"personid\":\"500226218651\"},\"myTask\":{\"taskid\":8,\"meetid\":24,\"taskinfo\":\"机场迎接嘉宾\",\"workingtime\":8,\"numbers\":2}}]")
    @UserLoginToken
    public ResultBean getVoTasks(HttpServletRequest request){
        List<UserMeeting> userMeetingInfo = userMeetingService.findUserMeetingInfo(Check.getUserID(request));

        Map<String,List> map = new HashMap<>();
        for (UserMeeting userMeeting : userMeetingInfo){
            if (userMeeting.getType() == 4 || userMeeting.getType() == 5 || userMeeting.getType() == 6){
                map.put("tasks",voTaskService.getTasks(userMeeting.getMeetingid()));
            }
        }

        return ResultBean.success(map);
    }
    @GetMapping("task/my")
    @ApiOperation(value = "用户获取自己申请的的志愿志愿者活动信息表",
            notes = "参数：<br>1. meetingId"
    )
    @UserLoginToken
    public ResultBean getVoTasksByUer(Integer meetingId, HttpServletRequest request){
        Integer userId = Check.getUserID(request);
        Map<String,List> map = new HashMap<>();
        map.put("tasks",voUserTaskInfoService.getMyTaskInfo(userId));
        return ResultBean.success(map);
    }

}
