package meeting.meetingv1.service;

import meeting.meetingv1.mapper.MeetingMapper;
import meeting.meetingv1.mapper.UserMapper;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.MeetingExample;
import meeting.meetingv1.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class MeetingService {


    @Autowired
    UserMapper userMapper;

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
        return result;
    }

    public Meeting findById(int id) {
        return meetingMapper.selectByPrimaryKey(id);
    }


    //分页查询
    public List<Meeting> findMeetings(int offset, int limit) {
        List<Meeting> meetings = meetingMapper.selectMeetings(offset, limit);
        return meetings;
    }


    //    ordermode 0代表全部会议 1 代表过期的会议    2 代表没过期的会议
    public List<Meeting> findMeetings(int userId, int offset, int limit, int orderMode) {
//得到当前时间
        Date date = new Date();
        List<Meeting> meetings = meetingMapper.selectMeetings(offset, limit, orderMode, date);
        return meetings;
    }

}
