package meeting.meetingv1.service;

import meeting.meetingv1.mapper.UserMeetingMapper;
import meeting.meetingv1.mapper.VoluntaskMapper;
import meeting.meetingv1.mapper.VoluntinfoMapper;
import meeting.meetingv1.pojo.Voluntask;
import meeting.meetingv1.pojo.Voluntinfo;
import meeting.meetingv1.pojo.VoluntinfoExample;
import meeting.meetingv1.pojo.mybeans.UserTaskBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class VoUserTaskInfoService {
    @Autowired
    UserMeetingService userMeetingService;
    @Autowired
    UserMeetingMapper userMeetingMapper;
    @Autowired
    VoluntinfoMapper voluntinfoMapper;
    @Autowired
    VoluntaskMapper voluntaskMapper;
    public List<Voluntinfo> getTaskInfo(Integer userId,Integer mettingId){
        VoluntinfoExample example = new VoluntinfoExample();
        VoluntinfoExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userId);
        criteria.andMeetingidEqualTo(mettingId);

        return voluntinfoMapper.selectByExample(example);
    }

    public List<UserTaskBean> getMyTaskInfo(Integer userId){
        VoluntinfoExample example = new VoluntinfoExample();
        VoluntinfoExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userId);
        List<Voluntinfo> voluntinfos = voluntinfoMapper.selectByExample(example);
        List<UserTaskBean> userTaskBeans = new ArrayList<>();
        for (Voluntinfo voluntInfo : voluntinfos){
            userTaskBeans.add(
                    new UserTaskBean(
                            userMeetingMapper.getVoluntTypeFlag(userId,voluntInfo.getMeetingid()),
                            voluntInfo,
                            voluntaskMapper.selectByPrimaryKey(voluntInfo.getTaskid())
                    ));
        }
        return userTaskBeans;
    }
    public void add(Voluntinfo voluntinfo){
        if (voluntinfo.getTaskid() != null && voluntinfo.getPersonid() != null && voluntinfo.getStudentid() != null)
        {
            voluntinfoMapper.insert(voluntinfo);
        }
    }

}
