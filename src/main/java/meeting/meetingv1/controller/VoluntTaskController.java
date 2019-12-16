package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.Voluntask;
import meeting.meetingv1.service.VoTaskService;
import meeting.meetingv1.service.VoUserTaskInfoService;
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

    @GetMapping("tasks/{meetingId}")
    @ApiOperation(value = "获取会议相关的志愿工作详情")
    public ResultBean getVoTasks(@PathVariable Integer meetingId){
        Map<String,List> map = new HashMap<>();
        map.put("tasks",voTaskService.getTasks(meetingId));
        return ResultBean.success(map);
    }
    @GetMapping("task/my")
    @ApiOperation(value = "用户获取自己申请的的志愿志愿者活动信息表")
    @UserLoginToken
    public ResultBean getVoTasksByUer(Integer meetingId, HttpServletRequest request){
        Integer userId = Check.getUserID(request);
        Map<String,List> map = new HashMap<>();
        map.put("tasks",voUserTaskInfoService.getMyTaskInfo(userId));
        return ResultBean.success(map);
    }

}
