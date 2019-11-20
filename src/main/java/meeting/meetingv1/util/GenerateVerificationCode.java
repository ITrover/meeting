package meeting.meetingv1.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenerateVerificationCode {
    static private String[] NUMBER = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    static private String[] NUMBER_CASE_Up_Down= new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private static String generate(String[] a, Integer len){
        List<String> list = Arrays.asList(a);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        String afterShuffle = sb.toString();
        return afterShuffle.substring(0,len);
    }

    /**
     * 默认生成6位数字随机码
     * @return
     */
    public static String getVerificationCode_NUM(){
        return generate(NUMBER,6);
    }

    /**
     * 生成指定位数的数字随机码
     * @param len
     * @return
     */
    public static String getVerificationCode_NUM(Integer len){
        return generate(NUMBER,len);
    }

    /**
     * 六位数字字母验证码
     * @return
     */
    public static String getVerificationCode_NUM_CASE(){
        return generate(NUMBER_CASE_Up_Down,6);
    }

    /**
     * 指定位数
     * @param len
     * @return
     */
    public static String getVerificationCode_NUM_CASE(Integer len){
        return generate(NUMBER_CASE_Up_Down,len);
    }
}
