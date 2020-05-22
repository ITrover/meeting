package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.DriverKey;
import meeting.meetingv1.pojo.Room;
import meeting.meetingv1.pojo.RoomKey;
import meeting.meetingv1.service.RoomService;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "房间操作接口")
public class RoomController {
    @Autowired
    RoomService roomService;
    @Autowired
    UserMeetingService userMeetingService;
    @ApiOperation(value = "增加房间",notes =
            "参数：房型，房间号，价格，会议号"
    )
    @UserLoginToken
    public ResultBean addRoom(String roomType, String roomNumber, String roomPrice, Integer meetingId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        //
        Room room = new Room(null, meetingId, roomType, roomNumber, roomPrice, null);
        roomService.addRoom(room);
        return ResultBean.success();
    }
    @PostMapping("/removeRoom")
    @UserLoginToken
    @ApiOperation(value = "删除房间",notes =
            "参数：会议Id，房间ID(查询时有返回)，请求头touken字段"
    )
    public ResultBean deleteRoom(Integer meetingId,Integer roomId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        roomService.deleteRoom(new RoomKey(roomId,meetingId));
        return ResultBean.success();
    }
    @PostMapping("/assignRoom")
    @UserLoginToken
    @ApiOperation(value = "分配房间",notes =
            "参数：会议Id，请求头touken字段。返回 房间-嘉宾 Map"
    )
    public ResultBean assignedRooms(Integer roomId,Integer meetingId,Integer guestId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        roomService.assignRoomForGuest(roomId,meetingId,guestId);
        return ResultBean.success();
    }
    @PostMapping("/assignedRooms")
    @UserLoginToken
    @ApiOperation(value = "查看所有分配信息",notes =
            "参数：会议Id，请求头touken字段。返回 房间-嘉宾 Map,房间中guestId字段指分配的嘉宾ID，未分配为-1"
    )
    public ResultBean assignedRooms(Integer meetingId,Integer roomId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        Map assignInfos = roomService.getAssignInfos(meetingId);
        return ResultBean.success(assignInfos);
    }
    @PostMapping("/assignableRooms")
    @UserLoginToken
    @ApiOperation(value = "查看可用房型信息",notes =
            "参数：会议Id，请求头touken字段。"
    )
    public ResultBean assignedRooms(Integer meetingId,HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        List allSuitableRoom = roomService.getAllSuitableRoom(meetingId);
        return ResultBean.success(allSuitableRoom);
    }
    @PostMapping("/allRooms")
    @UserLoginToken
    @ApiOperation(value = "所有房间信息",notes =
            "参数：会议Id，请求头touken字段。"
    )
    public ResultBean all(Integer meetingId,HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        List allSuitableRoom = roomService.allRooms(meetingId);
        return ResultBean.success(allSuitableRoom);
    }
    @PostMapping("/statisticRoom")
    @UserLoginToken
    @ApiOperation(value = "统计房型信息",notes =
            "参数：会议Id，请求头touken字段。"
    )
    public ResultBean statistic(Integer meetingId,HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        Map map = roomService.statistic(meetingId);
        return ResultBean.success(map);
    }
}

