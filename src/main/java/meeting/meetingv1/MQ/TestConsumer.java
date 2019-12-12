package meeting.meetingv1.MQ;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {

    @KafkaListener(topics = "test2")
    public void listen(ConsumerRecord record){
        System.out.println("收到信息 ： "+record.value());
    }
}
