package meeting.meetingv1.service;

import meeting.meetingv1.mapper.VoluntaskMapper;
import meeting.meetingv1.pojo.Voluntask;
import meeting.meetingv1.pojo.VoluntaskExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoTaskService {
    @Autowired
    VoluntaskMapper voluntaskMapper;
    public void addTask(Voluntask[] voluntasks,Integer meetingId) {
        for (Voluntask task:voluntasks){
            task.setMeetid(meetingId);
            voluntaskMapper.insert(task);
        }
    }
    public List<Voluntask> getTasks(Integer meetingId){

        VoluntaskExample voluntaskExample = new VoluntaskExample();
        VoluntaskExample.Criteria criteria = voluntaskExample.createCriteria();
        criteria.andMeetidEqualTo(meetingId);

        return voluntaskMapper.selectByExample(voluntaskExample);
    }
    public Voluntask getTaskByID(Integer taskId){
        return voluntaskMapper.selectByPrimaryKey(taskId);
    }
}
