package meeting.meetingv1.config;

import meeting.meetingv1.converter.StringToDateConverter;
import meeting.meetingv1.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * @auther NMID
 */
public class MyConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 通过registry来注册拦截器，通过addPathPatterns来添加拦截路径
        registry.addInterceptor(this.myInterceptor).addPathPatterns("/**");
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
    }
}