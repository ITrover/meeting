package meeting.meetingv1.service;

import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.mapper.UserMeetingMapper;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.pojo.UserMeetingExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMeetingService {
    @Autowired
    UserMeetingMapper userMeetingMapper;
    public int getCount(Byte type, Integer meetingid){
        return userMeetingMapper.count(type, meetingid);
    }
    public boolean addRelation(UserMeeting userMeeting) throws ParameterException {
        if (userMeeting.getType() != 2 && userMeeting.getType() != 3){
            throw new ParameterException();
        }
        userMeetingMapper.insert(userMeeting);
        return true;
    }
    public boolean deleteRelation(UserMeeting userMeeting){
        userMeetingMapper.delete(userMeeting);
        return true;
    }
    public List<UserMeeting> findPreferenceByUser(Integer userId, Byte type){
        UserMeetingExample userMeetingExample = new UserMeetingExample();
        UserMeetingExample.Criteria criteria = userMeetingExample.createCriteria();
        criteria.andUseridEqualTo(userId);
        criteria.andTypeEqualTo(type);
        List<UserMeeting> list = userMeetingMapper.selectByExample(userMeetingExample);
        return list;
    }
    public List<UserMeeting> findPreferenceByMeet(Integer meetId, Byte type){
        UserMeetingExample userMeetingExample = new UserMeetingExample();
        UserMeetingExample.Criteria criteria = userMeetingExample.createCriteria();
        criteria.andMeetingidEqualTo(meetId);
        criteria.andTypeEqualTo(type);
        List<UserMeeting> list = userMeetingMapper.selectByExample(userMeetingExample);
        return list;
    }
}
