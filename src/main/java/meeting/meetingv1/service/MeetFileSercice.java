package meeting.meetingv1.service;

import meeting.meetingv1.mapper.MeetingfileMapper;
import meeting.meetingv1.pojo.Meetingfile;
import meeting.meetingv1.pojo.MeetingfileExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetFileSercice {
    @Autowired
    MeetingfileMapper meetingfileMapper;
    public boolean addFile(String fileName,String path,Integer meetId) {
        meetingfileMapper.insert(new Meetingfile(null,meetId,path,fileName));
        return true;
    }
    public Integer deleteFile(Integer fileID) {
        Meetingfile meetingfile = meetingfileMapper.selectByPrimaryKey(fileID);
        meetingfileMapper.deleteByPrimaryKey(fileID);
        return meetingfile.getMeetingid();
    }
//    @Cacheable(cacheNames = {"Meetingfile"},key = "#meetID")
    public List<Meetingfile> getFileInfoByMeetID(Integer meetID){
        MeetingfileExample meetingfileExample = new MeetingfileExample();
        MeetingfileExample.Criteria criteria = meetingfileExample.createCriteria();
        criteria.andMeetingidEqualTo(meetID);
        return meetingfileMapper.selectByExample(meetingfileExample);
//        return meetingfileMapper.selectByMeetingId(meetID);
    }
}
