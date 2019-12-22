package meeting.meetingv1.MQ;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.Message;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Timer;

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

    @KafkaListener(topics = "test2")
    public void listen1(ConsumerRecord record) throws JsonProcessingException {
//        logger.info("收到信息！！！！！");
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
    @KafkaListener(topics = "VolunStatusInfo")
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
//        System.out.println("收到信息 ： "+record.value());
//        System.out.println(userById.getEmailaddr()+":"+message.getContent());
        //将信息存入数据库
        msgService.sendMsg(message);
//        System.out.println("收到信息 ： "+record.value());
    }
}
