package meeting.meetingv1;

import meeting.meetingv1.mapper.GuestMapper;
import meeting.meetingv1.mapper.MeetingMapper;
import meeting.meetingv1.pojo.Guest;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.Volunt;
import meeting.meetingv1.service.GuestService;
import meeting.meetingv1.service.MeetingService;
import meeting.meetingv1.service.VoluntService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class DaoTest {
    @Autowired
    GuestMapper guestMapper;
    @Autowired
    GuestService guestService;
    @Autowired
    MeetingService meetingService;
    @Autowired
    MeetingMapper meetingMapper;
    @Autowired
    VoluntService voluntService;

    @Test
    public void guesttest() {
        Guest guest = new Guest();
        guest.setAvatarUrl("/jd.com");
        guest.setIntroduction("介绍");
        guest.setGuestid(4);
        guest.setMeetingid(4);
//        guestMapper.insert(guest);
        guestService.addguest(guest);
        List<Guest> byMeetingId = guestService.findByMeetingId(1);
        System.out.println(byMeetingId.get(0));

    }

    @Test
    public  void meeetingtest() {
        Meeting meeting = new Meeting();
        meeting.setMeetingid(11);
        meeting.setCloseTime(new Date());
        meeting.setStartTime(new Date());
        meeting.setIntroduction("meeting 介绍");
        meeting.setmName("会议1");
        meeting.setLocation("地点");
        meeting.setNeedvolunteer(1);
//        meetingService.addMeeting(meeting);
//        Meeting byId = meetingService.findById(6);
//        System.out.println(byId);
        List<Meeting> meetings = meetingService.findMeetings(3, 2);
        int i = meetingMapper.selectMeetingRows();
        System.out.println(i);
        System.out.println(meetings);
    }
    @Test
    public  void  volunt(){
        Volunt volunt = new Volunt();
        volunt.setNumber(1);
        volunt.setIntroduction("介绍");
        volunt.setIsproof(1);
        volunt.setMeetid(1);
        volunt.setVolunt(3);
//        voluntService.addVolunt(volunt);
        Volunt volunt1 = voluntService.selectByMeetingId(1);
        System.out.println(volunt1);
    }
}


