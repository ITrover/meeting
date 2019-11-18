package meeting.meetingv1.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import meeting.meetingv1.pojo.User;

public class JwtService {
    public static String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getUserid().toString())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
