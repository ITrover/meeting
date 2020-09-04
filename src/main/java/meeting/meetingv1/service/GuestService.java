package meeting.meetingv1.service;

import meeting.meetingv1.mapper.GuestMapper;
import meeting.meetingv1.pojo.Guest;
import meeting.meetingv1.pojo.GuestExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GuestService {
    @Autowired
    GuestMapper guestMapper;
    public int addGuest(Guest guest){
        if(guest==null){
         throw new IllegalArgumentException("参数不能为空!");
        }
        int i = guestMapper.insert(guest);
        return i;
    }
    public boolean updateGuest(Guest guest){
        if(guest.getMeetingid() == null || guest.getGuestid() == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        Guest guest1 = guestMapper.selectByPrimaryKey(guest);
        if (guest.getAvatarUrl() != null){
            guest1.setAvatarUrl(guest.getAvatarUrl());
        }
        if (guest.getIntroduction() != null){
            guest1.setIntroduction(guest.getIntroduction());
        }
        guestMapper.updateByPrimaryKey(guest1);
        return true;
    }
    public boolean deleteGuest(Guest guest){

        if(guest.getMeetingid() == null || guest.getGuestid() == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        guestMapper.deleteByPrimaryKey(guest);
        return true;
    }
    @Cacheable(cacheNames = {"guestListFormeeting"},key = "#meetingid")
    public List<Guest> findByGuestMeetingId(int meetingid){
        GuestExample guestExample = new GuestExample();
        guestExample.createCriteria().andMeetingidEqualTo(meetingid);
        List<Guest> guests = guestMapper.selectByExample(guestExample);
        return guests;
    }
}
