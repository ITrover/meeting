package meeting.meetingv1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(value = "meeting.meetingv1.mapper")
@SpringBootApplication
@EnableCaching
@EnableSwagger2
public class MettingV1Application  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MettingV1Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        //传入SpringBoot应用的主程序       
        return application.sources(MettingV1Application.class);
    }
}
