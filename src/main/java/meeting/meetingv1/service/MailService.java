package meeting.meetingv1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    JavaMailSender mailSender;
    public boolean SendToMail(String mailAddr,String messageBody) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("meeting.group@qq.com");
        mailMessage.setTo(mailAddr);
        mailMessage.setSubject("[会务管理系统通知]");
        mailMessage.setText(messageBody);
        mailSender.send(mailMessage);
        return true;
    }
}
