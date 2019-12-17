package meeting.meetingv1.service;

import meeting.meetingv1.exception.ParameterException;
import meeting.meetingv1.mapper.VoluntMapper;
import meeting.meetingv1.pojo.Volunt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
//志愿活动
@Service
public class VoluntEventService {
    @Autowired
    VoluntMapper voluntMapper;



    public boolean addVoluntEvent(Volunt volunt) throws ParameterException {
        try {
            voluntMapper.insert(volunt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException();
        }
        return true;
    }
    @Cacheable(cacheNames = {"volunt"},key = "#meetId")
    public Volunt getVoEventByMeetingId(Integer meetId){
        return voluntMapper.selectByPrimaryKey(meetId);
    }
    @Cacheable(cacheNames = {"volunt"},key = "#result.meetid")
    public Volunt updateVo(Volunt volunt){
        Volunt volunt1 = this.getVoEventByMeetingId(volunt.getMeetid());
        if (volunt.getNumber() != null){
            volunt1.setNumber(volunt.getNumber());
        }
        if (volunt.getIntroduction()!=null){
            volunt1.setIntroduction(volunt.getIntroduction());
        }
        if (volunt.getIsproof()!=null){
            volunt1.setIsproof(volunt.getIsproof());
        }
        if (volunt.getFull()!=null){
            volunt1.setFull(volunt.getFull());
        }
        voluntMapper.updateByPrimaryKey(volunt1);
        return volunt1;
    }
}
