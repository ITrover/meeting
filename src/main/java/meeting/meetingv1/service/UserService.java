package meeting.meetingv1.service;

import meeting.meetingv1.mapper.UserMapper;
import meeting.meetingv1.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public User findUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
