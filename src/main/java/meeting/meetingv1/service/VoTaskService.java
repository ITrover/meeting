package meeting.meetingv1.service;

import meeting.meetingv1.mapper.VoluntaskMapper;
import meeting.meetingv1.pojo.Voluntask;
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
}
