package meeting.meetingv1.service;

import meeting.meetingv1.exception.IncorrectCredentialsException;
import meeting.meetingv1.exception.SignUpColumnException;
import meeting.meetingv1.exception.UnknownAccountException;
import meeting.meetingv1.mapper.UserMapper;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User findUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 登陆验证，验证成功返回生成的JWT字符串
     * @param key
     * @param password
     * @return
     * @throws IncorrectCredentialsException
     * @throws UnknownAccountException
     */
    public String loginCheck(String key, String password) throws IncorrectCredentialsException, UnknownAccountException {
        boolean isMail = key.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        UserExample userExample = new UserExample();
        UserExample.Criteria userExampleCriteria = userExample.createCriteria();
        List<User> userList = null;
        if (isMail){
            userExampleCriteria.andEmailaddrEqualTo(key);
            userList = userMapper.selectByExample(userExample);
        }else{
            userExampleCriteria.andPhoneEqualTo(key);
            userList = userMapper.selectByExample(userExample);
        }
        if (userList.isEmpty()){
            throw new UnknownAccountException();
        }else if (password.equals(userList.get(0).getPassword())){
            return JwtService.getToken(userList.get(0));
        }else {
            throw new IncorrectCredentialsException();
        }
    }

    /**
     * 注册成功将Usermail对应的缓存删除
     * @param user
     * @return
     * @throws SignUpColumnException
     */
    public boolean signUp(User user) throws SignUpColumnException {

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw  new SignUpColumnException();
        }
        return true;
    }
//    public String sendVerificationCode
}
