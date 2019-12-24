package meeting.meetingv1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.mapper.UserMeetingMapper;
import meeting.meetingv1.pojo.*;
import meeting.meetingv1.service.MeetingService;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.MQ.KafkaSender;
import meeting.meetingv1.service.VoUserTaskInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class DaoTest {
//    @Autowired
//    GuestMapper guestMapper;
//    @Autowired
//    GuestService guestService;
//    @Autowired
//    MeetingService meetingService;
//    @Autowired
//    MeetingMapper meetingMapper;
//    @Autowired
//    VoluntService voluntService;
//
//    @Test
//    public void guesttest() {
//        Guest guest = new Guest();
//        guest.setAvatarUrl("/jd.com");
//        guest.setIntroduction("介绍");
//        guest.setGuestid(5);
//        guest.setMeetingid(5);
////        guestMapper.insert(guest);
//        guestService.addguest(guest);
//        List<Guest> byMeetingId = guestService.findByMeetingId(1);
//        System.out.println(byMeetingId.get(0));
//
//    }
//
//    @Test
//    public  void meeetingtest() {
//        Meeting meeting = new Meeting();
//        meeting.setMeetingid(12);
//        meeting.setCloseTime(new Date());
//        meeting.setStartTime(new Date());
//        meeting.setIntroduction("meeting 介绍");
//        meeting.setmName("会议1");
//        meeting.setLocation("地点");
//        meeting.setNeedvolunteer(1);
//        meetingService.addMeeting(meeting);
//        Meeting byId = meetingService.findById(12);
//        System.out.println(byId);
//        List<Meeting> meetings = meetingService.findMeetings(3, 2);
//        int i = meetingMapper.selectMeetingRows();
//        System.out.println(i);
//        System.out.println(meetings);
//    }
//    @Test
//    public  void  volunt(){
//        Volunt volunt = new Volunt();
//        volunt.setNumber(2);
//        volunt.setIntroduction("介绍");
//        volunt.setIsproof(1);
//        volunt.setMeetid(2);
//        volunt.setVolunt(3);
//        voluntService.addVolunt(volunt);
//        Volunt volunt1 = voluntService.selectByMeetingId(1);
//        System.out.println(volunt1);
//    }
    @Autowired
    UserMeetingService userMeetingService;
//    @Test
    void name() throws ParameterException {
        UserMeeting userMeeting = new UserMeeting();
        userMeeting.setUserid(1);
        userMeeting.setMeetingid(1);
        Byte b = 2;
        userMeeting.setType(b);
        userMeetingService.addRelation(userMeeting);

        System.out.println(userMeeting.getId());
    }

    @Autowired
    KafkaSender kafkaSender;
//    @Test
    void kafkaSender() throws JsonProcessingException {
//        kafkaSender.sendMsg("test2","测试消息2");
//        VolunStatusInfo volunStatusInfo = new VolunStatusInfo();
//        volunStatusInfo.setMeetingId(1)
//                .setType(6)
//                .setUserId(2);
//        kafkaSender.sendMsg("VolunStatusInfo",objectMapper.writeValueAsString(volunStatusInfo));
//        System.out.println("发送完成");
    }
    @Autowired
    ObjectMapper objectMapper;
    @Test
    void ceshiJs() throws JsonProcessingException, UnsupportedEncodingException {
//        Event event = new Event();
//        event.setMessage(new Message(null, 0, 1, 2, "测试消息"));
//        ObjectMapper objectMapper = new ObjectMapper();
        List<Voluntask> strings = new ArrayList<>();
        Voluntask voluntask = new Voluntask();
        voluntask.setTaskinfo("a2s1d2ass1d2as1d21");
        voluntask.setNumbers(2);
        voluntask.setWorkingtime(8);
        strings.add(voluntask);
        voluntask.setTaskinfo("测试是的 的的的的");
        voluntask.setNumbers(2);
        voluntask.setWorkingtime(8);
        strings.add(voluntask);

        String json = objectMapper.writeValueAsString(strings);
        System.out.println(json);
        String encode = URLEncoder.encode(json, "utf-8");
        System.out.println(encode);
        System.out.println(URLDecoder.decode(encode,"utf-8"));

        String json2 = "[{\"taskinfo\":\"测试是的 的的的的\",\"workingtime\":8,\"numbers\":2},{\"taskinfo\":\"测试是的 的的的的\",\"workingtime\":8,\"numbers\":2}]";

        Voluntask[] event1 = objectMapper.readValue(json, Voluntask[].class);
        System.out.println(event1.length);
//        System.out.println(event1.getMessage().getContent());
//        System.out.println(event1.getMessage());
    }
    @Autowired
    MeetingService service;
//    @Test
    void name4() {
        Meeting meeting = new Meeting();
        meeting.setmName("asdasd");
        meeting.setLocation("测试地点");
        meeting.setIntroduction("sddsfsdf");
        meeting.setNeedvolunteer(1);
        service.addMeeting(meeting);
        System.out.println(meeting.getMeetingid()
        );
    }
    @Autowired
    UserMeetingMapper userMeetingMapper;
    @Autowired
    VoUserTaskInfoService voUserTaskInfoService;
//    @Test
    void name5() throws JsonProcessingException {
        Meeting meeting = new Meeting();
        String json = objectMapper.writeValueAsString(userMeetingMapper.getVolunteers(24));
        System.out.println(json);
        System.out.println();
//        System.out.println(
//                objectMapper.writeValueAsString(voUserTaskInfoService.getMyTaskInfo(17)));
    }

//    @Test
    void name6() throws JsonProcessingException, UnsupportedEncodingException {
        Meeting meeting = new Meeting();
        meeting.setStartTime(new Date());
        meeting.setmName("第一次会议");
        meeting.setIntroduction("huijianjiena");
        meeting.setLocation("第三教学楼");
        String string = objectMapper.writeValueAsString(meeting);
//        string = "{\"meetingid\":null,\"mName\":null,\"location\":null,\"startTime\":\"2019-12-22\",\"closeTime\":null,\"introduction\":null,\"schedule\":null,\"needvolunteer\":null,\"typeid\":null,\"organizer\":null,\"hostedby\":null,\"communicate\":null}";
        String encode = URLEncoder.encode(string, "utf-8");
        System.out.println(string);
        Meeting meeting1 = objectMapper.readValue(string, Meeting.class);
        Date startTime = meeting.getStartTime();
        System.out.println(encode);
    }
}


