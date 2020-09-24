package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import meeting.meetingv1.annotation.SysLog;
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

/**
 * 司机操作接口
 *
 * @author Lydia
 * @date 2020/9/21
 */
@RestController
@Api(tags = "司机操作接口")
public class DriverController {

    @Autowired
    UserMeetingService userMeetingService;
    @Autowired
    DriverService driverService;

    @PostMapping("/addDriver")
    @ApiOperation(value = "增加司机",notes =
            "参数：会议Id，车牌号，司机姓名，司机电话，车型，请求头token字段"
    )
    public ResultBean addDriver(@ApiParam(name = "meetingId",value = "会议Id") Integer meetingId,
                                @ApiParam(name = "carId",value = "汽车Id") String carId,
                                @ApiParam(name = "driverName",value = "司机名")String driverName,
                                @ApiParam(name = "driverTel",value = "司机电话号码") String driverTel,
                                @ApiParam(name = "carType",value = "汽车种类")String carType,
                                HttpServletRequest request){
        Driver driver = new Driver(null,meetingId,carId,driverName,driverTel,carType,null);
        driverService.addDriver(driver);
        return ResultBean.success();
    }


    @PostMapping("/removeDriver")
    @ApiOperation(value = "删除司机",notes =
            "参数：会议Id，司机ID(查询时有返回)，请求头token字段"
    )
    public ResultBean deleteDriver(@ApiParam(name = "meetingId",value = "会议Id")Integer meetingId,
                                   @ApiParam(name = "driverId",value = "司机Id")Integer driverId,
                                   HttpServletRequest request){
        DriverKey driverKey = new DriverKey(driverId,meetingId);
        driverService.deleteDriver(driverKey);
        return ResultBean.success();
    }

    @PostMapping("/assignDriver")
    @ApiOperation(value = "分配司机",notes =
            "参数：会议Id，司机ID(查询时有返回)，嘉宾ID，请求头touken字段"
    )
    public ResultBean assignDriver(@ApiParam(name = "driverId",value = "司机Id")Integer driverId,
                                   @ApiParam(name = "guestId",value = "嘉宾Id")Integer guestId,
                                   HttpServletRequest request){
        driverService.assignDriverForGuest(driverId,guestId);
        return ResultBean.success();
    }

    @PostMapping("/assignableDrivers")
    @ApiOperation(value = "查看所有可用司机信息",notes =
            "参数：会议Id，请求头token字段" +
                    "<br>返回 司机-嘉宾 Map"
    )
    public ResultBean assignableDrivers(@ApiParam(name = "meetingId",value = "会议Id")Integer meetingId,
                                        HttpServletRequest request){
        List list = driverService.assignableDrivers(meetingId);
        HashMap<String,List> map = new HashMap<>(16);
        map.put("list",list);
        return ResultBean.success(map);
    }

    @PostMapping("/allDrivers")
    @ApiOperation(value = "查看所有司机信息",notes =
            "参数：会议Id，请求头token字段" +
                    "<br>返回 司机-嘉宾 Map"
    )
    public ResultBean allDrivers(@ApiParam(name = "meetingId",value = "会议Id")Integer meetingId,
                                 HttpServletRequest request){
        List list = driverService.allDrivers(meetingId);
        HashMap<String,List> map = new HashMap<>(16);
        map.put("list",list);
        return ResultBean.success(map);
    }

    @PostMapping("/assignInfo")
    @ApiOperation(value = "分配详情",notes =
            "参数：会议Id，请求头token字段" +
                    "<br>返回 司机-嘉宾 Map"
    )
    public ResultBean assignInfo(@ApiParam(name = "meetingId",value = "会议Id")Integer meetingId,
                                 HttpServletRequest request){
        Map<Driver, Guest> map = driverService.assignedDriverInfos(meetingId);
        return ResultBean.success(map);
    }
}
