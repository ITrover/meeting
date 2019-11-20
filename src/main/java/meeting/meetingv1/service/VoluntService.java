package meeting.meetingv1.service;

import com.sun.mail.imap.protocol.ID;
import meeting.meetingv1.mapper.VoluntMapper;
import meeting.meetingv1.pojo.Volunt;
import meeting.meetingv1.pojo.VoluntExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoluntService {
    @Autowired
    VoluntMapper voluntMapper;
    public  int addVolunt(Volunt volunt){
        if(volunt==null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        int i = voluntMapper.insert(volunt);
        return i;
    }
    public Volunt selectByMeetingId(int meetingid){

        VoluntExample voluntExample = new VoluntExample();
        voluntExample.createCriteria().andMeetidEqualTo(meetingid);
        List<Volunt> volunts = voluntMapper.selectByExample(voluntExample);
        Volunt volunt = volunts.get(0);
        return volunt;

    }

}
