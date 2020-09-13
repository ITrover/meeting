package meeting.meetingv1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author MaYunHao
 * @version 1.0
 * @date 2020/9/5 18:23
 * @description
 */
@SpringBootTest
public class FileTest {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Value("${user.avatar.path}")
    String avater;
    @Test
    void filePathTest(){
    }
}
