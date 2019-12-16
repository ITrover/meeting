package meeting.meetingv1.service;

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
    VoluntinfoMapper voluntinfoMapper;
    @Autowired
    VoluntaskMapper voluntaskMapper;
    public List<Voluntinfo> getMyTaskInfo(Integer userId){
        VoluntinfoExample example = new VoluntinfoExample();
        VoluntinfoExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userId);
        List<Voluntinfo> voluntinfos = voluntinfoMapper.selectByExample(example);
        List<UserTaskBean> userTaskBeans = new ArrayList<>();
        for (Voluntinfo voluntInfo : voluntinfos){
            userTaskBeans.add(new UserTaskBean(voluntInfo,voluntaskMapper.selectByPrimaryKey(voluntInfo.getTaskid())));
        }
        return voluntinfos;
    }
    public void add(Voluntinfo voluntinfo){
        if (voluntinfo.getTaskid() != null && voluntinfo.getPersonid() != null && voluntinfo.getStudentid() != null)
        {
            voluntinfoMapper.insert(voluntinfo);
        }
    }

}
