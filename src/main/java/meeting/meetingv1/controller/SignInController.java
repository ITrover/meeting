package meeting.meetingv1.controller;

import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.IncorrectCredentialsException;
import meeting.meetingv1.exception.UnknownAccountException;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.service.SignInService;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.service.UserService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.soap.Addressing;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api(tags = "签到操作接口")
public class SignInController {
    @Autowired
    UserMeetingService userMeetingService;

    //创建或更新
    @PostMapping("/addSignIn")
    @UserLoginToken
    @ResponseBody
    public ResultBean addSign(Integer meetingId, String signInStartTime,HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
            date = simpleDateFormat.parse(signInStartTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResultBean.error(-1,"请使用yyyy-MM-dd HH:mm形式时间字符串！");
        }
        signInService.addSign(date,meetingId);
        return ResultBean.success();
    }
    //获取会议签到码
//    @UserLoginToken
    @GetMapping("/getSignPic/{meetingId}")
    public void getSignPic(HttpServletRequest request, HttpServletResponse response,@PathVariable Integer meetingId) throws IOException, WriterException {
//        if (!Check.checkUp(request,userMeetingService,meetingId)){
//            return;
//        }
        ServletOutputStream outputStream = response.getOutputStream();
        signInService.getSignPic(outputStream,meetingId);
    }
    @GetMapping("/statisticSignInInfo")
    @UserLoginToken
    @ResponseBody
    public ResultBean statisticSignInInfo(Integer meetingId,HttpServletRequest request){
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        Collection collection = signInService.SignStatistic(meetingId);
        Map<String,Collection> infos = new HashMap<>();
        infos.put("infos",collection);
        return ResultBean.success(infos);
    }
    @Autowired
    SignInService signInService;
    @Autowired
    UserService userService;
    //成员签到
    @RequestMapping("sign/{meetingId}/{code}")
    @ResponseBody
    public ResultBean signIn(
            @PathVariable Integer meetingId,
            @PathVariable String code
            ,String key,String password) throws IncorrectCredentialsException, UnknownAccountException {
        User user = userService.getLoginUser(key, password);
        if (!signInService.signInAvailable(meetingId,code)){
            return ResultBean.error(-1,"签到失败");
        }
        signInService.doSign(user.getUserid(),meetingId);
        return ResultBean.success();
    }

    @RequestMapping("goSignPage/{meetingId}/{code}")

    public ModelAndView signPage(
            @PathVariable Integer meetingId,
            @PathVariable String code){
        Map<String ,String > map = new HashMap<>();
        map.put("meetingId",meetingId.toString());
        map.put("code",code);
        System.out.println(map.toString());
        //实现方式 二维码链接到这个控制器中，跳转到下一个静态页面，携带
        return new ModelAndView("signPage","map",map);
    }
//    @RequestMapping("/test1")
//    public String test(){
//        return "test";
//    }
}
