package meeting.meetingv1.controller;

import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.Guest;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.pojo.Volunt;
import meeting.meetingv1.service.GuestService;
import meeting.meetingv1.service.MeetingService;
import meeting.meetingv1.service.VoluntService;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MeetingController {
    @Autowired
    MeetingService meetingService;
    @Autowired
    VoluntService voluntService;
    @Autowired
    GuestService guestService;


    @ResponseBody
    @RequestMapping(path = "meeting/add", method = RequestMethod.POST)
    public ResultBean publishMeeting(Meeting meeting, Volunt volunt, List<Guest> guests) {
//        System.out.println(meeting+" "+volunt+guests);
        meetingService.addMeeting(meeting);
        voluntService.addVolunt(volunt);
        for (int i = 0; i < guests.size(); i++) {
            guestService.addguest(guests.get(i));
        }
//        guestService.addguest(guests);
        return ResultBean.success();
    }

//    通过id得到会议详情页
    @ResponseBody
    @RequestMapping(path = "meeting/{meetingId}", method = RequestMethod.POST)
    public  ResultBean getMeetingDetail(@PathVariable("meetingId") int id){
        Meeting meeting = meetingService.findById(id);
        Map map = new HashMap();
        Volunt volunt = voluntService.selectByMeetingId(id);
        List<Guest> guests = guestService.findByMeetingId(id);
        map.put("meeting",meeting);
        map.put("volunt",volunt);
        map.put("guests",guests);
        return ResultBean.success(map);
    }


    @ResponseBody
    @RequestMapping(path = "/hello1", method = RequestMethod.POST)
    public ResultBean hello(Meeting meeting) {
        System.out.println(meeting);
        return ResultBean.success();
    }
}
