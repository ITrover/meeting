package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.pojo.Volunt;
import meeting.meetingv1.service.MeetingService;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.service.VoluntEventService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "会议的志愿活动相关接口")
public class VoluntEventController {
    @Autowired
    VoluntEventService voluntEventService;
    @Autowired
    UserMeetingService userMeetingService;

    @GetMapping("volunt")
    @ApiOperation(value = "获取会议志愿活动信息",notes = "参数： 1、会议ID meetingId  <a href=\"http://www.ljhhhx.com/voluntInfo.png\">实体字段介绍截图</a>")
    public ResultBean getVoluntEventInfo(Integer meetingId){
        Map<String, Volunt> map = new HashMap<>();
        map.put("info",voluntEventService.getVoEventByMeetingId(meetingId));
        return ResultBean.success(map);
    }
    @PostMapping("volunt")
    @UserLoginToken
    @ApiOperation(value = "创建者更改会议志愿活动信息",notes = "参数： 1、<a href=\"http://www.ljhhhx.com/voluntInfo.png\">实体字段介绍截图</a> 这里所有字段都是不是必须（传什么修改什么，除了meetId）<br>2、登陆token<br>")
    public ResultBean getVoluntEventInfo(Volunt volunt, HttpServletRequest request) throws ParameterException {
        if (!Check.checkUp(request,userMeetingService,volunt.getMeetid())){
            return ResultBean.error(-12,"无权限");
        }
        voluntEventService.addVoluntEvent(volunt);
        return ResultBean.success();
    }
    @PostMapping("addVolunt")
    @UserLoginToken
    @ApiOperation(value = "创建者发布会议相关的志愿活动",notes = "参数： 1、<a href=\"http://www.ljhhhx.com/voluntInfo.png\">实体字段介绍截图</a> 注意：只有是否停止招募非必须<br>2、登陆token<br>")
    public ResultBean addVoluntEventInfo(Volunt volunt, HttpServletRequest request) {
        if (!Check.checkUp(request,userMeetingService,volunt.getMeetid())){
            return ResultBean.error(-12,"无权限");
        }
        voluntEventService.updateVo(volunt);
        return ResultBean.success();
    }
    @PostMapping("joinVolunteer/{meetingId}")
    @UserLoginToken
    @ApiOperation(value = "用户申请志愿者",notes = "参数： <br>1、会议id meetingId <br>2、登陆token<br>")
    public ResultBean join(@PathVariable Integer meetingId, HttpServletRequest request) throws ParameterException {
        Byte b = 4;//4 为申请志愿者
        userMeetingService.addRelation(new UserMeeting(null,Check.getUserID(request),meetingId,b));
        return ResultBean.success();
    }


}