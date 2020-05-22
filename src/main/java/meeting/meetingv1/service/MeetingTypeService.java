package meeting.meetingv1.service;

import meeting.meetingv1.mapper.MeetingtypeMapper;
import meeting.meetingv1.pojo.Meetingtype;
import meeting.meetingv1.pojo.MeetingtypeExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MeetingTypeService {
    @Autowired
    MeetingtypeMapper meetingtypeMapper;
    public List<Meetingtype> getTypes(){
        MeetingtypeExample example = new MeetingtypeExample();
        MeetingtypeExample.Criteria criteria = example.createCriteria();
        criteria.andTypeidIsNotNull();
        return meetingtypeMapper.selectByExample(example);
    }
}
