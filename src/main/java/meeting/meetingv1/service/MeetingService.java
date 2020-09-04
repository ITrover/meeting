package meeting.meetingv1.service;

import com.sun.mail.imap.protocol.ID;
import meeting.meetingv1.mapper.MeetingMapper;
import meeting.meetingv1.mapper.MeetingtypeMapper;
import meeting.meetingv1.mapper.UserMapper;
import meeting.meetingv1.mapper.UserMeetingMapper;
import meeting.meetingv1.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MeetingService {


    @Autowired
    UserMapper userMapper;
    @Autowired
    UserMeetingMapper UserMeetingMapper;
    @Autowired
    MeetingtypeMapper meetingtypeMapper;

    public User findUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Autowired
    MeetingMapper meetingMapper;

    public int addMeeting(Meeting meeting) {
        if (meeting == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        int result = meetingMapper.insert(meeting);
        return meeting.getMeetingid();//更新：返回插入的会议ID
    }

    @Cacheable(cacheNames = "meetingsByID",key = "#id")
    public Meeting findById(int id) {
        return meetingMapper.selectByPrimaryKey(id);
    }


    //首页分页查询
    public List<Meeting> findMeetings(int offset, int limit) {
        List<Meeting> meetings = meetingMapper.selectMeetings(offset, limit);
        return meetings;
    }
    public Integer countOfMeetings(){
        int i = meetingMapper.selectMeetingRows();
        return i;
    }

    //和用户相关会议 没做分页
    public List<Meeting> findMeetingsByUserId(int userid) {
        List<Meeting> meetings = new ArrayList<>();
        UserMeetingExample userMeetingExample = new UserMeetingExample();
        userMeetingExample.createCriteria().andUseridEqualTo(userid);
        List<UserMeeting> userMeetings = UserMeetingMapper.selectByExample(userMeetingExample);
        for (int i = 0; i < userMeetings.size(); i++) {
            Integer meetingid = userMeetings.get(i).getMeetingid();
            Meeting meeting = meetingMapper.selectByPrimaryKey(meetingid);
            meetings.add(meeting);
        }
        return meetings;
    }

    public List<Meeting> findMeetingsByUserIdAndTime(int userid, int mode) {
        List<Meeting> meetings = new ArrayList<>();
        UserMeetingExample userMeetingExample = new UserMeetingExample();
        userMeetingExample.createCriteria().andUseridEqualTo(userid);
        List<UserMeeting> userMeetings = UserMeetingMapper.selectByExample(userMeetingExample);
        Byte thistype = (byte)mode;
        for (int i = 0; i < userMeetings.size(); i++) {
            Integer meetingid = userMeetings.get(i).getMeetingid();
            if (userMeetings.get(i).getType().equals(thistype)){
                meetings.add(meetingMapper.selectByPrimaryKey(meetingid));
            }
//            if (mode == 1) {
//                MeetingExample meetingExample = new MeetingExample();
//                meetingExample.createCriteria().andStartTimeLessThan(new Date()).andMeetingidEqualTo(meetingid);
//                Meeting meeting = meetingMapper.selectByExample(meetingExample).get(0);
//                meetings.add(meeting);
//            }
//            if (mode == 0) {
//                MeetingExample meetingExample = new MeetingExample();
//                meetingExample.createCriteria().andStartTimeGreaterThan(new Date()).andMeetingidEqualTo(meetingid);
//                Meeting meeting = meetingMapper.selectByExample(meetingExample).get(0);
//                meetings.add(meeting);
//            }
        }
        return meetings;
    }

    //查询会议类型
    public int findMeetingsType(String type) {
        MeetingtypeExample meetingtypeExample = new MeetingtypeExample();
        meetingtypeExample.createCriteria().andTypeEqualTo(type);
        List<Meetingtype> meetingtypes = meetingtypeMapper.selectByExample(meetingtypeExample);
        if (meetingtypes.size()==0){
            return -1;
        }
        Integer typeid = meetingtypes.get(0).getTypeid();
        return typeid;
    }

    //动态查询
//这里还差一个日期的限制.and....还有type没有查出来
    public List<Meeting> findMeetingsDy(Integer type, String location, Date beginTime,Date endTime,String name) {
        MeetingExample meetingExample = new MeetingExample();
        MeetingExample.Criteria criteria = meetingExample.createCriteria();//这里还差一个日期的限制.and....
        if (type != null){
            criteria.andTypeidEqualTo(type);
        }
        if (location != null){
            criteria.andLocationLike(location);
        }
        if (beginTime != null){
            criteria.andStartTimeLessThanOrEqualTo(beginTime);
        }
        if (endTime != null){
            criteria.andStartTimeGreaterThanOrEqualTo(endTime);
        }
        if (name != null){
            criteria.andMNameLike(name);
        }
        List<Meeting> meetings = meetingMapper.selectByExample(meetingExample);
        return meetings;
    }


    //    ordermode 0代表全部会议 1 代表过期的会议    2 代表没过期的会议
    public List<Meeting> findMeetings(int offset, int limit, int orderMode) {
        //得到当前时间
        Date date = new Date();
        List<Meeting> meetings = meetingMapper.selectMeetings(offset, limit, orderMode, date);
        return meetings;
    }
}
