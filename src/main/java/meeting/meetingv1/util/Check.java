package meeting.meetingv1.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import meeting.meetingv1.exception.VerificationException;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.UserMeetingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class Check {
    public static boolean checkUp(HttpServletRequest httpServletRequest, UserMeetingService userMeetingService,Integer meetingId){
        HttpSession session = httpServletRequest.getSession(false);
        Integer userId = (Integer)session.getAttribute("userId");
        List<UserMeeting> meetingsByBuilder = userMeetingService.getMeetingsByBuilder(userId);
        boolean flag = false;
        if (meetingsByBuilder.size() == 0){
            return false;
        }
        for (UserMeeting userMeeting :meetingsByBuilder){
            if (userMeeting.getMeetingid() == (meetingId)){
                return true;
            }
        }
        return false;
    }
    public static Integer getUserID(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        if (session==null){
            return null;
        }
        Object userId1 = session.getAttribute("userId");
        Integer userId = userId1==null?null:(Integer)userId1;
        if (userId == null){    
            String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
            String userIdStr;
            try {
                userIdStr = JWT.decode(token).getAudience().get(0);
                userId = new Integer(userIdStr);
            } catch (JWTDecodeException j) {
                userId= null;
            }
        }
        return userId;
    }

}
