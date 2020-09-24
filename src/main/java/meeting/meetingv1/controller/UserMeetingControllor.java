package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.pojo.mybeans.UserPublicInfos;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.service.UserService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
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

/**
 * 用户与会议的交互接口
 * @author NMID
 */
@RestController
@Api(tags = "用户与会议的交互接口")
@Slf4j
public class UserMeetingControllor {

    final
    UserMeetingService userMeetingService;

    public UserMeetingControllor(UserMeetingService userMeetingService, UserService userService) {
        this.userMeetingService = userMeetingService;
        this.userService = userService;
    }

    @PostMapping("/preference/{type}")
    @ApiOperation(value = "参加/收藏会议",notes = "参数： <br>1、登陆token<br>2、路径变量操作类型（2为参加 3为收藏 ）<br>3、会议id meetingId")
    public ResultBean addPreference(
            @ApiParam(name = "type",value = "参加/收藏会议，路径变量操作类型（2为参加 3为收藏 ）") @PathVariable("type") Integer type,
            @ApiParam(name = "meetingId",value = "会议Id") Integer meetingId,
            @ApiParam(name = "request",value = "登陆token") HttpServletRequest request) throws ParameterException {
        userMeetingService.addRelation(new UserMeeting(Check.getUserID(request),meetingId,new Byte(type.toString())));
        return ResultBean.success();
    }
    @PostMapping("/quit/{type}/{meetingId}")
    @ApiOperation(value = "退出会议 取消收藏会议",notes = "参数： <br>1、登陆token<br>2、操作类型 type （2为参加 3为收藏 ）<br>3、路径变量会议id meetingId")
    public ResultBean preference(
            @ApiParam(name = "meetingId",value = "会议Id") Integer meetingId,
            @ApiParam(name = "type",value = "参加/收藏会议，路径变量操作类型（2为参加 3为收藏 ）") @PathVariable Integer type,
            @ApiParam(name = "request",value = "登陆token") HttpServletRequest request) throws ParameterException {
        userMeetingService.delete(new UserMeeting(Check.getUserID(request),meetingId,new Byte(type.toString())));
        return ResultBean.success();
    }
    /***
     * type 1为创建 2为参加 3为收藏
     * 根据用户查询对应的
     * @param type 类型
     * @param request 登陆token
     * @return status
     */
    @GetMapping("/preference/{type}")
    @ApiOperation(value = "获取与自己相关的会议信息",notes = "参数： <br>1、路径变量中类型：（1为创建 2为参加 3为收藏）<br>2、登陆toke" +
            "<br>一个成功的请求：" +
            "{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;\"code\": 0,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"success\",\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;\"data\": {\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"data\": [\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\": 10,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"userid\": 17,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"meetingid\": 1,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"type\": 2\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
            "} " +
            "<br>其中data字段是一个数组")
    public ResultBean getPreferenceByUser(
            @ApiParam(name = "type",value = "参加/收藏会议，路径变量操作类型（2为参加 3为收藏 ）") @PathVariable("type") String type,
            @ApiParam(name = "request",value = "登陆token") HttpServletRequest request){
        Map<String, List> map = new HashMap<>();
        List<UserMeeting> byMeet = userMeetingService.findPreferenceByUser(Check.getUserID(request), new Byte(type));
        map.put("data",byMeet);
        return ResultBean.success(map);
    }
    final
    UserService userService;
    @GetMapping("/meetPreference/{type}")
    @ApiOperation(value = "主办方获取会议 被参加、收藏、申请志愿者的详细信息"
            ,notes = "参数： <br>1、会议id meetingId2、路径变量操作类型（2为参加 3为收藏），登陆token" +
            "<br>包括参会者的userName、手机、邮箱")
    public ResultBean getPreference(
            @ApiParam(name = "type",value = "参加/收藏会议，路径变量操作类型（2为参加 3为收藏 ）") @PathVariable("type") String type,
            @ApiParam(name = "meetingId",value = "会议Id") Integer meetingId,
            @ApiParam(name = "request",value = "登陆token") HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        Map<String, List> map = new HashMap<>();
        List<UserMeeting> byMeet = userMeetingService.findPreferenceByMeet(meetingId, new Byte(type));
        List<UserPublicInfos> list = new ArrayList<>();
        for (UserMeeting userMeeting : byMeet) {
            list.add(new UserPublicInfos(userService.findUserById(userMeeting.getUserid())));
        }
        map.put("data",list);
        return ResultBean.success(map);
    }

    @GetMapping("/statistics/{type}")
    @ApiOperation(value = "获取会议被参加、收藏的次数",notes = "参数： <br>1、会议id meetingId<br>2、路径变量操作类型（2为参加 3为收藏）" +
            "<br>data字段中count为次数")
    public ResultBean getCount(
            @ApiParam(name = "type",value = "参加/收藏会议，路径变量操作类型（2为参加 3为收藏 ）") @PathVariable("type") String type,
            @ApiParam(name = "meetingId",value = "会议Id") Integer meetingId){
        int count = userMeetingService.getCount(new Byte(type), meetingId);
        Map<String,Integer> map = new HashMap<>();
        map.put("count",count);
        return ResultBean.success(map);
    }
    //
}
