package meeting.meetingv1.service;

import meeting.meetingv1.mapper.GuestMapper;
import meeting.meetingv1.pojo.Guest;
import meeting.meetingv1.pojo.GuestExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GuestService {
    @Autowired
    GuestMapper guestMapper;
    public int  addguest(Guest guest){
        if(guest==null){
         throw new IllegalArgumentException("参数不能为空!");
        }
        int i = guestMapper.insert(guest);
        return i;
    }
    public  List<Guest> findByMeetingId(int meetingid){
        GuestExample guestExample = new GuestExample();
        guestExample.createCriteria().andMeetingidEqualTo(meetingid);
        List<Guest> guests = guestMapper.selectByExample(guestExample);
        return guests;
    }
}
