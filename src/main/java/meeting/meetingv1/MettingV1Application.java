package meeting.meetingv1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(value = "meeting.meetingv1.mapper")
@SpringBootApplication
@EnableCaching
@EnableSwagger2
public class MettingV1Application {

    public static void main(String[] args) {
        SpringApplication.run(MettingV1Application.class, args);
    }

}
