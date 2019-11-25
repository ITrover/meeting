package meeting.meetingv1.exception.handler;

import meeting.meetingv1.exception.*;
import com.aliyuncs.exceptions.ClientException;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class WebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler
    public ResultBean signUpException(SignUpColumnException e) {
        log.error("注册字段错误", e);
        return ResultBean.error(-1, "注册字段错误");
    }

    @ExceptionHandler
    public ResultBean unknownAccount(UnknownAccountException e) {
        log.error("账号不存在", e);
        return ResultBean.error(-2, "账号不存在");
    }

    @ExceptionHandler
    public ResultBean incorrectCredentials(IncorrectCredentialsException e) {
        log.error("密码错误", e);
        return ResultBean.error(-3, "密码错误");
    }
    @ExceptionHandler
    public ResultBean mailSendException(MailException e) {
        log.error("邮件发送失败", e);
        return ResultBean.error(-4, "邮件发送失败");
    }

    @ExceptionHandler
    public ResultBean smsException(ClientException e) {
        log.error("短信发送失败", e);
        // 发送邮件通知技术人员.
        return ResultBean.error(-5, "短信发送失败");
    }
    @ExceptionHandler
    public ResultBean verificationException(VerificationException e) {
        log.error("验证码错误", e);
        return ResultBean.error(-6, "验证码错误");
    }


    @ExceptionHandler
    public ResultBean unknownException(Exception e) {
        log.error("发生了未知异常", e);
        // 发送邮件通知技术人员.
        return ResultBean.error(-99, "系统出现错误, 请联系网站管理员!");
    }
    @ExceptionHandler
    public ResultBean unknownException(ParameterException e) {
        log.error("参数错误", e);
        // 发送邮件通知技术人员.
        return ResultBean.error(-9, "参数不合法");
    }
}
