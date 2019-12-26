package meeting.meetingv1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.MQ.KafkaSender;
import meeting.meetingv1.MQ.SendToMany;
import meeting.meetingv1.MQ.VolunStatusInfo;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.pojo.*;
import meeting.meetingv1.pojo.mybeans.VolunteerInfo;
import meeting.meetingv1.service.*;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "会议的志愿活动相关接口")
public class VoluntEventController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    VoluntEventService voluntEventService;
    @Autowired
    UserMeetingService userMeetingService;
    @Autowired
    VoUserTaskInfoService voUserTaskInfoService;
    @Autowired
    VoTaskService voTaskService;
    @Autowired
    KafkaSender kafkaSender;
    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("volunt/{meetingId}")
    @ApiOperation(value = "获取会议志愿活动信息",notes = "参数： 1、会议ID meetingId  <a href=\"http://www.ljhhhx.com/voluntInfo.png\">实体字段介绍截图</a>")
    public ResultBean getVoluntEventInfo(Integer meetingId){
        Map<String, Volunt> map = new HashMap<>();

        map.put("info",voluntEventService.getVoEventByMeetingId(meetingId));
        return ResultBean.success(map);
    }
    @GetMapping("volunt/info/{meetingId}")
    @ApiOperation(value = "获取会议志愿活动具体的工作内容信息",notes = "参数： 1、会议ID meetingId  <a href=\"http://www.ljhhhx.com/voluntInfo.png\">实体字段介绍截图</a>")
    public ResultBean getVoTaskeInfo(Integer meetingId){
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
    @ApiOperation(value = "用户申请志愿者",notes = "参数： <br>1、会议id meetingId" +
            "<br>2、登陆token" +
            "<br>3. 用户学号(可选)" +
            "<br>4. 用户身份证号(可选)" +
            "<br>5. 工作序号 taskid" )
    public ResultBean join(@PathVariable Integer meetingId, Voluntinfo voluntinfo, HttpServletRequest request) throws ParameterException {
        Byte b = 4;//4 为申请志愿者
        logger.info("用户申请志愿者，学号:"+voluntinfo.getStudentid());
        userMeetingService.addRelation(new UserMeeting(null,Check.getUserID(request),meetingId,b));
        voUserTaskInfoService.add(voluntinfo);
        logger.info("志愿信息储存完毕");
        return ResultBean.success();
    }

    @GetMapping("joinVolunteer/{meetingId}")
//    @UserLoginToken
    @ApiOperation(value = "会议发起者查看 会议-志愿关系 的关联 ",notes = "参数： <br>1、会议id meetingId <br>2、登陆token " +
            "<br>" +
            "<br>返回，type字段 4 志愿者 5 申请待批准 6 申请被拒绝 创建例子：" +
            "<br>[{\"id\":86,\"userid\":17,\"meetingid\":24,\"type\":4},{\"id\":88,\"userid\":2,\"meetingid\":24,\"type\":5}]")
    public ResultBean sayYES(@PathVariable Integer meetingId, HttpServletRequest request) throws JsonProcessingException {
//        if (!(Check.checkUp(request,userMeetingService,meetingId)))
//        {
//            return ResultBean.error(-12,"无权限");
//        }
        Map<String, List> map = new HashMap<>();
        List<UserMeeting> byMeet = userMeetingService.getVolunteers(meetingId);
        System.out.println(objectMapper.writeValueAsString(byMeet));;
        map.put("info",byMeet);
        return ResultBean.success(map);
    }
    @GetMapping("volunteer/info/{meetingId}")
    @UserLoginToken
    @ApiOperation(value = "会议发起者查看申请志愿者的详细信息",notes = "参数： <br>1、会议id meetingId <br>2、登陆token <br>数据部分的info是用户和会议的对应信息，当然这里只有会议的组织者才能查看" +
            "<br>返回例子：" +
            "{\"code\":0,\"message\":\"success\",\"data\":{\"infos\":[{\"userId\":17,\"meetingId\":24,\"type\":4,\"taskId\":8,\"studentId\":\"123123\",\"personId\":\"500226218651\",\"taskInfo\":\"机场迎接嘉宾\"},{\"userId\":2,\"meetingId\":24,\"typeFlag\":5,\"taskId\":7,\"studentId\":\"2017210000\",\"personId\":\"123111111\",\"taskInfo\":\"会场管理\"}]}}"
    )
    public ResultBean getInfo(@PathVariable Integer meetingId, HttpServletRequest request) throws JsonProcessingException {
        if (!(Check.checkUp(request,userMeetingService,meetingId)))
        {
            return ResultBean.error(-12,"无权限");
        }
        Map<String, List> map = new HashMap<>();
        List<VolunteerInfo> infos = new ArrayList<>();
        List<UserMeeting> byMeet = userMeetingService.getVolunteers(meetingId);
        for (UserMeeting userMeeting : byMeet){
            List<Voluntinfo> taskInfo = voUserTaskInfoService.getTaskInfo(userMeeting.getUserid(), userMeeting.getMeetingid());
            for (Voluntinfo voluntinfo :taskInfo){
                Voluntask taskByID = voTaskService.getTaskByID(voluntinfo.getTaskid());
                infos.add(new VolunteerInfo(voluntinfo.getUserid(),voluntinfo.getMeetingid(),userMeeting.getType(),
                        voluntinfo.getTaskid(),
                        voluntinfo.getStudentid(),
                        voluntinfo.getPersonid(),
                        taskByID.getTaskinfo()
                        ));
            }
        }
//        System.out.println(objectMapper.writeValueAsString(byMeet));;
        map.put("infos",infos);
        return ResultBean.success(map);
    }


    @PostMapping("volunteerStatus/{meetingId}")
    @UserLoginToken
    @ApiOperation(value = "会议组织者通过或拒绝志愿者请求",notes =
            "参数： <br>1、会议id meetingId " +
                    "<br>2、登陆token " +
                    "<br>3. 操作对象用户的ID userId" +
                    "<br>4. 操作指令 type 5：通过为志愿者 6：拒绝成为志愿者数据部分的info是用户和会议的对应信息，当然这里只有会议的组织者才能查看")
    public ResultBean passRequest(@PathVariable Integer meetingId, HttpServletRequest request ,Integer userId,int type) throws ParameterException, JsonProcessingException {
        if (!(Check.checkUp(request,userMeetingService,meetingId)) || (type != 6 && type != 5))
        {
            return ResultBean.error(-12,"无权限");
        }
        UserMeeting userMeeting = new UserMeeting();
        userMeeting.setType(new Byte((byte) type));
        userMeeting.setMeetingid(meetingId);
        userMeeting.setUserid(userId);
        userMeetingService.updateVolunteer(userMeeting);
        VolunStatusInfo volunStatusInfo = new VolunStatusInfo();
        volunStatusInfo.setMeetingId(meetingId)
                .setType(type)
                .setUserId(userId);
        kafkaSender.sendMsg("VolunStatusInfo",objectMapper.writeValueAsString(volunStatusInfo));

        return ResultBean.success();
    }
    @PostMapping("broadcast/{meetingId}")
    @UserLoginToken
    @ApiOperation(value = "会议组织者向志愿者发送广播消息",notes = "参数： <br>1、会议id meetingId <br>2、登陆token <br>3. 消息内容 content <br>4 志愿者的状态类型 type  4:正在申请 5:通过申请")
    public ResultBean broadcast(@PathVariable Integer meetingId, HttpServletRequest request,String content,Integer type) throws ParameterException, JsonProcessingException {
        if (!(Check.checkUp(request,userMeetingService,meetingId)))
        {
            return ResultBean.error(-12,"无权限");
        }
        if (type != 4 && type != 5){
            return ResultBean.error(-12,"无权限");
        }
        SendToMany many = new SendToMany(meetingId,content,type);

        kafkaSender.sendMsg("test2",objectMapper.writeValueAsString(many));
        return ResultBean.success();
    }
}