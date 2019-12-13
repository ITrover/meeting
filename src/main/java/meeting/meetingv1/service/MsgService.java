package meeting.meetingv1.service;

import meeting.meetingv1.mapper.MessageMapper;
import meeting.meetingv1.pojo.Message;
import meeting.meetingv1.pojo.MessageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgService {
    @Autowired
    MessageMapper messageMapper;

    public void sendMsg(Message message){
        messageMapper.insert(message);
    }
    public List<Message> getMsgByUserID(Integer userId){
        MessageExample example = new MessageExample();
        MessageExample.Criteria criteria = example.createCriteria();
        criteria.andSendtoEqualTo(userId);
        return messageMapper.selectByExample(example);
    }
}
