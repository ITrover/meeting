package meeting.meetingv1.service;

import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.mapper.UserMeetingMapper;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.pojo.UserMeetingExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMeetingService {
    @Autowired
    UserMeetingMapper userMeetingMapper;
    public int getCount(Byte type, Integer meetingid){
        return userMeetingMapper.count(type, meetingid);
    }

    public List<UserMeeting> getMeetingsByBuilder(Integer userId){
        UserMeetingExample example = new UserMeetingExample();
        UserMeetingExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userId);
        Byte type = 1;//查询创建的会议
        criteria.andTypeEqualTo(type);
        return userMeetingMapper.selectByExample(example);
    }
    public boolean addRelation(UserMeeting userMeeting) throws ParameterException {
        if (userMeeting.getType() != 2 && userMeeting.getType() != 3){
            throw new ParameterException();
        }
        userMeetingMapper.insert(userMeeting);
        return true;
    }
    //i 1 为申请志愿者
    public boolean addRelation(UserMeeting userMeeting,int i) throws ParameterException {
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
    //改变申请志愿者状态
    public boolean updateVolunteer(UserMeeting userMeeting) throws ParameterException{
        UserMeetingExample userMeetingExample = new UserMeetingExample();
        UserMeetingExample.Criteria criteria = userMeetingExample.createCriteria();
        criteria.andTypeEqualTo((byte)4);
        criteria.andUseridEqualTo(userMeeting.getUserid());
        criteria.andMeetingidEqualTo(userMeeting.getMeetingid());
        List<UserMeeting> userMeetings = userMeetingMapper.selectByExample(userMeetingExample);
        if (userMeetings.size() != 1 || (userMeeting.getType()!=5 && userMeeting.getType()!=6))
        {
            throw new ParameterException();
        }
        UserMeeting userMeeting1 = userMeetings.get(0);
        userMeeting1.setType(userMeeting.getType());
        userMeetingMapper.insert(userMeeting1);
        return true;
    }
}
