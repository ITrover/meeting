package meeting.meetingv1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.Voluntask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Component
//@Log4j
public class JsonUtil<V>{
    @Autowired
    ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public Object decodeUTF8JsonToObject(String jsonString,Class target) throws UnsupportedEncodingException, JsonProcessingException {
        String decode = URLDecoder.decode(jsonString, "utf-8");
        logger.info("解码后Json字符串："+decode);
        return objectMapper.readValue(decode, target);
    }
//    public <T> T decodeUTF8JsonToObject(String jsonString) throws UnsupportedEncodingException, JsonProcessingException {
//        String decode = URLDecoder.decode(jsonString, "utf-8");
//        return objectMapper.readValue(decode, new T().getClass());
//    }
public Meeting decodeUTF8JsonToMeeting(String jsonString) throws UnsupportedEncodingException, JsonProcessingException {
    String decode = URLDecoder.decode(jsonString, "utf-8");
    logger.info("解码后Json字符串："+decode);
    return objectMapper.readValue(decode, Meeting.class);
}
}
