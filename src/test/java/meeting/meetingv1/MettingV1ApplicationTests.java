package meeting.meetingv1;

import meeting.meetingv1.mapper.UserMapper;
import meeting.meetingv1.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MettingV1ApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    UserMapper userMapper;
    @Test
    void teat1() {
        User user = new User();
//        user.setCreattime(new Long(System.currentTimeMillis()).toString());
//        user.setGroups("重庆邮电大学");
//        user.setMail("1083775096@qq.com");
//        user.setName("吕进豪");
//        user.
        user.setEmailaddr("1083775096@qq.com");
        user.setUsername("lvlvlvlv");
        user.setPhone("15023654968");
        user.setGender("男");
        user.setPassword("123456");
        System.out.println(userMapper.insert(user));

    }
}
