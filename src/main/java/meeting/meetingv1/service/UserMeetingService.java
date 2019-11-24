package meeting.meetingv1.service;

import meeting.meetingv1.mapper.UserMeetingMapper;
import meeting.meetingv1.pojo.UserMeeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMeetingService {
    @Autowired
    UserMeetingMapper userMeetingMapper;
    public boolean addRelation(UserMeeting userMeeting){
        userMeetingMapper.insert(userMeeting);
        return true;
    }
    public boolean deleteRelation(UserMeeting userMeeting){
        userMeetingMapper.delete(userMeeting);
        return true;
    }
}
