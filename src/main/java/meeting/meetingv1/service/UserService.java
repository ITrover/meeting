package meeting.meetingv1.service;

import meeting.meetingv1.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User findUserById(String userId) {
        return new User();
    }
}
