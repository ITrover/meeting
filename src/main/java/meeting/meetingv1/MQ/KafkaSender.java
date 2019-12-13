package meeting.meetingv1.MQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 生产者
 * @author Minko
 */
@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;
    /**
     * 发送消息到kafka test2
     *@param messageBody 内容体
     */
    public void sendMsg(String topic,Event messageBody) throws JsonProcessingException {
        kafkaTemplate.send(topic,null,objectMapper.writeValueAsString(messageBody));
    }
    public void sendMsg(String topic,String messageBody) throws JsonProcessingException {
        kafkaTemplate.send(topic,null,messageBody);
    }
}
