package meeting.meetingv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
/**
 * @auther NMID
 */
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("meeting.meetingv1.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("会议管理系统v1 接口文档")
                .description("欢迎使用 " +
                        "<br><strong>测试用token</strong>：eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxNyJ9.5Hub-OLEmgTRaTpvdClJh3i9JQpY1UdlxU0Ls6JzW_A" +
                        "<br><strong>返回的Json格式：</strong><br>{<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;\"code\": 0,<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"success\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;\"data\": null<br>"+
                        "}"+
                        "<br>code 状态码，0为成功，负数为错误码"+
                        "<br>message 错误信息"+
                        "<br>data 数据部分"+
                        "<br><strong>例如使用info接口查询到的返回信息如下</strong>"+
                        "<br>{<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;\"code\": 0,<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"success\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;\"data\": {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"info\": {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"userid\": 17,<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"realname\": \"吕进豪\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"username\": \"L J H\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"gender\": \"男\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"emailaddr\": \"108214@qq.com\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"phone\": \"15086924104\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"organization\": \"重邮\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\": \"4a84a2dc-ad96-4ccf-a605-4e5624235495.png\",<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"password\": \"123456\"<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
//                .termsOfServiceUrl("https://github.com/YOhonour")
                .contact("l")
                .version("1.0")
                .build();
    }
}
