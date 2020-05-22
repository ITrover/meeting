package meeting.meetingv1.MQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.Message;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestConsumer {



    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MsgService msgService;
    @Autowired
    MailService mailService;
    @Autowired
    MeetingService meetingService;
    @Autowired
    UserService userService;
    @Autowired
    UserMeetingService userMeetingService;

//    @KafkaListener(topics = "test2")
    public void listen1(ConsumerRecord record) throws JsonProcessingException {
        SendToMany sendToMany = objectMapper.readValue((String) record.value(), SendToMany.class);
        List<UserMeeting> preferenceByMeet = userMeetingService.findPreferenceByMeet(sendToMany.getMeetingId(), (byte) (int) sendToMany.getType());
        Message message = new Message();
        Meeting meeting = meetingService.findById(sendToMany.getMeetingId());
        for (UserMeeting userMeeting : preferenceByMeet){
            message.setContent("[来自 "+meeting.getmName()+" 主办方的信息]："+sendToMany.getContent());
            message.setSendfrom(0);
            message.setIsread(-1);
            message.setSendto(userMeeting.getUserid());
            User userById = userService.findUserById(userMeeting.getUserid());
            mailService.SendToMail(userById.getEmailaddr(),message.getContent());
            msgService.sendMsg(message);
        }
    }
//    @KafkaListener(topics = "VolunStatusInfo")
    public void listen(ConsumerRecord record) throws JsonProcessingException {
        VolunStatusInfo volunStatusInfo = objectMapper.readValue((String) record.value(), VolunStatusInfo.class);
        Message message = new Message();
        Meeting meeting = meetingService.findById(volunStatusInfo.getMeetingId());
        message.setContent("[系统消息] 用户您好！您的["+meeting.getmName()+"]志愿者申请"+
                (volunStatusInfo.getType()== 5 ? "成功通过，请持续关注主办方信息！" : "很遗憾未能通过，敬请关注其他活动，感谢您的参与！"));
        message.setSendto(volunStatusInfo.getUserId());
        message.setSendfrom(0);
        message.setIsread(-1);
        User userById = userService.findUserById(volunStatusInfo.getUserId());
        //发送邮件
        mailService.SendToMail(userById.getEmailaddr(),message.getContent());
        msgService.sendMsg(message);
    }
    public void listen2test2(SendToMany sendToMany) throws JsonProcessingException {
        List<UserMeeting> preferenceByMeet = userMeetingService.findPreferenceByMeet(sendToMany.getMeetingId(), (byte) (int) sendToMany.getType());
        Message message = new Message();
        Meeting meeting = meetingService.findById(sendToMany.getMeetingId());
        for (UserMeeting userMeeting : preferenceByMeet){
            message.setContent("[来自 "+meeting.getmName()+" 主办方的信息]："+sendToMany.getContent());
            message.setSendfrom(0);
            message.setIsread(-1);
            message.setSendto(userMeeting.getUserid());
            User userById = userService.findUserById(userMeeting.getUserid());
            mailService.SendToMail(userById.getEmailaddr(),message.getContent());
            msgService.sendMsg(message);
        }
    }
    public void listen2VolunStatusInfo(VolunStatusInfo volunStatusInfo) throws JsonProcessingException {
        Message message2 = new Message();
        Meeting meeting = meetingService.findById(volunStatusInfo.getMeetingId());
        message2.setContent("[系统消息] 用户您好！您的["+meeting.getmName()+"]志愿者申请"+
                (volunStatusInfo.getType()== 5 ? "成功通过，请持续关注主办方信息！" : "很遗憾未能通过，敬请关注其他活动，感谢您的参与！"));
        message2.setSendto(volunStatusInfo.getUserId());
        message2.setSendfrom(0);
        message2.setIsread(-1);
        User userById = userService.findUserById(volunStatusInfo.getUserId());
        //发送邮件
        mailService.SendToMail(userById.getEmailaddr(),message2.getContent());
        msgService.sendMsg(message2);
    }
}
