package meeting.meetingv1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.pojo.*;
import meeting.meetingv1.service.*;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.JsonUtil;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@Api(tags = "会议相关接口")
public class MeetingController {
    @Autowired
    MeetFileSercice meetFileSercice;
    @Autowired
    MeetingService meetingService;
    @Autowired
    VoluntEventService voluntService;
    @Autowired
    GuestService guestService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    VoTaskService voTaskService;
    @Autowired
    MeetingTypeService meetingTypeService;
    @Autowired
    UserMeetingService userMeetingService;
    @Autowired
    JsonUtil jsonUtil;
//    @Autowired
    Logger logger = LoggerFactory.getLogger(this.getClass());
    Byte CREAT_RELATION = 1;
    @ResponseBody
    @GetMapping("meetingTypes")
    public ResultBean getType(){
        Map<String,List> map = new HashMap<>();
        map.put("types",meetingTypeService.getTypes());
        return ResultBean.success(map);
    }
    @ResponseBody
    @RequestMapping(path = "meeting/count", method = RequestMethod.POST)
    @ApiOperation(value = "获取会议总数")
    public ResultBean countOfMeetings() {
        Integer integer = meetingService.countOfMeetings();
        HashMap<String ,Integer> map = new HashMap<>();
        map.put("count",integer);
        return ResultBean.success(map);
    }

    //还没测试
//    @ResponseBody
//    @UserLoginToken
//    @RequestMapping(path = "meeting/add", method = RequestMethod.POST)
//    public ResultBean publishMeeting(Meeting meeting, Volunt volunt, ArrayList<Guest> guests,
//    HttpServletRequest request
//    ) throws ParameterException {
//        System.out.println(meeting+""+volunt+guests);
////        System.out.println(meeting+" "+volunt+guests);
//        int meetingId = meetingService.addMeeting(meeting);
//        voluntService.addVoluntEvent(volunt);
//        for (int i = 0; i < guests.size(); i++) {
//            guests.get(i).setMeetingid(meetingId);
//            guestService.addGuest(guests.get(i));
//        }
//        //增加关系
//        userMeetingService.addRelation(new UserMeeting( Check.getUserID(request),meetingId,CREAT_RELATION));
////        guestService.addguest(guests);
//        return ResultBean.success();
//    }


    /**
     * 对应手机端的请求，日期格式：yyyy-MM-dd HH:mm:ss
     * @param meeting
     * @return
     * @throws ParameterException
     */
    @ResponseBody
    @UserLoginToken
    @RequestMapping(path = "meeting/put", method = RequestMethod.POST)
    @ApiOperation(value = "添加会议信息（文本部分）",notes =
            "参数： <br>1、会议实体序列化后的编码后（UTF8编码）Json串 ：meetingJson  " +
                    "<br>其中未编码的字符串如下" +
                    "<br>{\"meetingid\":null,\"mName\":null,\"location\":null," +
                    "<br>\"startTime\":null,\"closeTime\":null,\"introduction\":null," +
                    "<br>\"schedule\":null,\"needvolunteer\":null,\"typeid\":null,\"organizer\":null," +
                    "<br>\"hostedby\":null,\"communicate\":null}\n" +
                    "<br><a href=\"http://www.ljhhhx.com/meetingInfo.png\">注释截图</a><br>" +
                    "其中meetingid不用传，mName、location、introduction必须有，时间的字符串格式为：yyyy-MM-dd HH:mm:ss <br>" +
                    "2、会议任务的json字符串 introduction <br>" +
                    "两个任务的数组json例子：[{\"taskinfo\":\"测试是的 的的的的\",\"workingtime\":8,\"numbers\":2},{\"taskinfo\":\"测试是的 的的的的\",\"workingtime\":8,\"numbers\":2}]" +
                    "<br>注意：此处为原始字符串，在Http标准中不允许请求出现‘{’‘}’等字符，需要将原始字符串再使用utf-8编码一次<br>" +
                    "编码后的例子(实际作为参数的字符串，显示不全F12看元素吧)：" +
                    "<br>" +
                    "%5B%7B%22taskid%22%3Anull%2C%22meetid%22%3Anull%2C%22taskinfo%22%3A%22%E6%B5%8B%E8%AF%95%E6%98%AF%E7%9A%84+%E7%9A%84%E7%9A%84%E7%9A%84%E7%9A%84%22%2C%22workingtime%22%3A8%2C%22numbers%22%3A2%7D%2C%7B%22taskid%22%3Anull%2C%22meetid%22%3Anull%2C%22taskinfo%22%3A%22%E6%B5%8B%E8%AF%95%E6%98%AF%E7%9A%84+%E7%9A%84%E7%9A%84%E7%9A%84%E7%9A%84%22%2C%22workingtime%22%3A8%2C%22numbers%22%3A2%7D%5D" +
                    "3、登陆token"
    )
    public ResultBean publishMeeting2(
            String meetingJson,
            String taskjson,
            HttpServletRequest request
            ) throws ParameterException, JsonProcessingException, UnsupportedEncodingException {

        logger.info("收到编码Json："+meetingJson);
        Meeting meeting = jsonUtil.decodeUTF8JsonToMeeting(meetingJson);

        Integer meetingId = meetingService.addMeeting(meeting);
        userMeetingService.addRelation(new UserMeeting(Check.getUserID(request),meetingId,CREAT_RELATION),true);
        if (taskjson != null){
            String decode = URLDecoder.decode(taskjson, "utf-8");
            Voluntask[] tasks = objectMapper.readValue(decode, Voluntask[].class);
            voTaskService.addTask(tasks,meetingId);
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("meetingId",meeting.getMeetingid());
        logger.info("会议添加完成"+meeting.toString());
        return ResultBean.success(map);
    }
//[{"taskinfo":"测试是的 的的的的","workingtime":8,"numbers":2},{"taskinfo":"测试是的 的的的的","workingtime":8,"numbers":2}]

    /**
     *
     * @param id
     * @return
     */
    //    通过id得到会议详情页1
    @ApiOperation(value = "根据会议id得到会议详细信息",notes = "参数： <br>1、会议ID meetingId   " )
    @ResponseBody
    @RequestMapping(path = "meeting/{meetingId}", method = RequestMethod.GET)
    public ResultBean getMeetingDetail(@PathVariable("meetingId") int id) {
        Meeting meeting = meetingService.findById(id);
        Map map = new HashMap();
        Volunt volunt = voluntService.getVoEventByMeetingId(id);
        List<Guest> guests = guestService.findByGuestMeetingId(id);
        List<Voluntask> tasks = voTaskService.getTasks(id);
        List<Meetingfile> fileInfoByMeetID = meetFileSercice.getFileInfoByMeetID(id);
        map.put("meeting", meeting);
        map.put("volunt", volunt);
        map.put("tasks",tasks);
        map.put("guests", guests);
        map.put("files",fileInfoByMeetID);
        return ResultBean.success(map);
    }
//    @ApiOperation(value = "根据会议id得到会议详细信息",notes = "参数： <br>1、会议ID meetingId   " )
//    @ResponseBody
//    @RequestMapping(path = "meeting/ids", method = RequestMethod.GET)
//    public ResultBean getMeetingDetails(String idListJson) throws UnsupportedEncodingException, JsonProcessingException {
//        logger.info("收到编码Json："+idListJson);
//        Integer[] meetings = (Integer[])jsonUtil.decodeUTF8JsonToObject(idListJson,Integer[].class);
//
//        Map map = new HashMap();
//        List<Meeting> list = new ArrayList<>();
//        for (Integer metId : meetings){
//            list.add(meetingService.findById(metId));
//        }
//        Volunt volunt = voluntService.getVoEventByMeetingId(id);
//        List<Guest> guests = guestService.findByGuestMeetingId(id);
//        map.put("meeting", meeting);
//        map.put("volunt", volunt);
//        map.put("guests", guests);
//        return ResultBean.success(map);
//    }

    //测试请求1
//    @ResponseBody
//    @RequestMapping(path = "/hello1", method = RequestMethod.POST)
    public ResultBean hello(Meeting meeting) {
        System.out.println(meeting);
        Map guestHashMap = new HashMap<>();
        Guest guest = new Guest();
        guest.setMeetingid(1);
        guest.setGuestid(4);
        guestHashMap.put("guest1",guest);
        guestHashMap.put("guest2",guest);
        return ResultBean.success(guestHashMap);
    }

//    分页获取首页会议1

    @ApiOperation(value = "根据会议首页分页的会议数据",notes = "参数: 1.起始位置 offset 2. 条数 limit")
    @ResponseBody
    @RequestMapping(path = "/meetings/home", method = RequestMethod.GET)
    public ResultBean findMeetings(int offset, int limit) {
        List<Meeting> meetings = meetingService.findMeetings(offset, limit);
        HashMap hashMap = new HashMap();
        hashMap.put("meetings", meetings);
        return ResultBean.success(hashMap);
    }

    //与我有关的会议+时间查询 没测通
    @ResponseBody
    @RequestMapping(path = "/meetings/my/{mode}", method = RequestMethod.GET)
    @ApiOperation(value = "查询自己有关的会议",notes = "mode可选 1、2、3、4、5")
    @UserLoginToken
    public ResultBean findMeetingsByUserIdAndTime(@PathVariable("mode") Integer mode,HttpServletRequest request) {
        Integer userID = Check.getUserID(request);
        List<Meeting> meetings = meetingService.findMeetingsByUserIdAndTime(userID,mode);
        HashMap hashMap = new HashMap();
        hashMap.put("meetings", meetings);
        return ResultBean.success(hashMap);
    }

    //与我有关会议1
    @ApiOperation(value = "根据id得到和我有关的会议",notes = "参数： <br>1、userid  我的id  " +
            " <br>2、登陆token  ")
    @UserLoginToken
    @ResponseBody
    @RequestMapping(path = "/meetings/my", method = RequestMethod.GET)
    public ResultBean findMeetingsByUserId(int userid) {
        List<Meeting> meetings = meetingService.findMeetingsByUserId(userid);
        HashMap hashMap = new HashMap();
        hashMap.put("meetings", meetings);
        return ResultBean.success(hashMap);
    }

    //    动态查询会议 类型 地点 时间1

    @ApiOperation(value = "根据条件查询会议",notes = "参数： <br>1、 type 会议类型 如 1 (科研)  " +
            " <br>2、location 会议地点 如逸夫楼 <br>3、date 会议日期 格式 yyyy-MM-dd  如2019-11-25 ")
    @ResponseBody
    @RequestMapping(path = "/meetings/dy", method = RequestMethod.GET)
    public ResultBean findMeetingsDy(@Nullable Integer type, @Nullable String location,
                                     @Nullable Date beginTime,@Nullable Date endTime,@Nullable String name) {
//        int meetingsType = meetingService.findMeetingsType(type);
        List<Meeting> meetings = meetingService.findMeetingsDy(type, location, endTime,beginTime, name);
        HashMap hashMap = new HashMap();
        hashMap.put("meetings", meetings);
        return ResultBean.success(hashMap);
    }


}
