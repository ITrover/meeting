package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.Driver;
import meeting.meetingv1.pojo.DriverKey;
import meeting.meetingv1.pojo.Guest;
import meeting.meetingv1.service.DriverService;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "司机操作接口")
public class DriverController {
    @Autowired
    UserMeetingService userMeetingService;
    @Autowired
    DriverService driverService;


    @PostMapping("/addDriver")
    @UserLoginToken
    @ApiOperation(value = "增加司机",notes =
            "参数：会议Id，车牌号，司机姓名，司机电话，车型，请求头touken字段"
    )
    public ResultBean addDriver(Integer meetingId,String carId, String driverName, String driverTel, String carType, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        Driver driver = new Driver(null,meetingId,carId,driverName,driverTel,carType,null);
        driverService.addDriver(driver);
        return ResultBean.success();
    }
    @PostMapping("/removeDriver")
    @UserLoginToken
    @ApiOperation(value = "删除司机",notes =
            "参数：会议Id，司机ID(查询时有返回)，请求头touken字段"
    )
    public ResultBean deleteDriver(Integer meetingId,Integer driverId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        DriverKey driverKey = new DriverKey(driverId,meetingId);
        driverService.deleteDriver(driverKey);
        return ResultBean.success();
    }
    @PostMapping("/assignDriver")
    @UserLoginToken
    @ApiOperation(value = "分配司机",notes =
            "参数：会议Id，司机ID(查询时有返回)，嘉宾ID，请求头touken字段"
    )
    public ResultBean assignDriver(Integer meetingId,Integer driverId,Integer guestId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        driverService.assignDriverForGuest(driverId,guestId);
        return ResultBean.success();
    }
    @PostMapping("/assignableDrivers")
    @UserLoginToken
    @ApiOperation(value = "查看所有可用司机信息",notes =
            "参数：会议Id，请求头touken字段" +
                    "<br>返回 司机-嘉宾 Map"
    )
    public ResultBean assignableDrivers(Integer meetingId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        List list = driverService.assignableDrivers(meetingId);
        HashMap<String,List> map = new HashMap();
        map.put("list",list);
        return ResultBean.success(map);
    }
    @PostMapping("/allDrivers")
    @UserLoginToken
    @ApiOperation(value = "查看所有司机信息",notes =
            "参数：会议Id，请求头touken字段" +
                    "<br>返回 司机-嘉宾 Map"
    )
    public ResultBean allDrivers(Integer meetingId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        List list = driverService.allDrivers(meetingId);
        HashMap<String,List> map = new HashMap();
        map.put("list",list);
        return ResultBean.success(map);
    }
    @PostMapping("/assignInfo")
    @UserLoginToken
    @ApiOperation(value = "分配详情",notes =
            "参数：会议Id，请求头touken字段" +
                    "<br>返回 司机-嘉宾 Map"
    )
    public ResultBean assignInfo(Integer meetingId, HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        Map<Driver, Guest> map = driverService.assignedDriverInfos(meetingId);
        return ResultBean.success(map);
    }
}
