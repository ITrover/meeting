package meeting.meetingv1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.meetingv1.pojo.Voluntask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Component
public class JsonUtil<V>{
    @Autowired
    ObjectMapper objectMapper;

    public Object decodeUTF8JsonToObject(String jsonString,Class target) throws UnsupportedEncodingException, JsonProcessingException {
        String decode = URLDecoder.decode(jsonString, "utf-8");
        return objectMapper.readValue(decode, target);
    }
//    public <T> T decodeUTF8JsonToObject(String jsonString) throws UnsupportedEncodingException, JsonProcessingException {
//        String decode = URLDecoder.decode(jsonString, "utf-8");
//        return objectMapper.readValue(decode, new T().getClass());
//    }
}
