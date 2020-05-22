package meeting.meetingv1.controller;

import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "创建或更新 对应会议的签到",notes =
            "没创建就新增，否则更新签到信息。参数：会议Id，签到开始时间（yyyy-MM-dd HH:mm）signInStartTime，请求头touken字段"
    )
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
    @ApiOperation(value = "签到获取二维码",notes =
            "二维码直接跳转到静态页面，携带参量信息包括meetingId、code(随机参数，在签到事件创建或更新时生成，意味着每次更新信息后之前的二维码将失效)" +
                    "参数：会议Id（路径变量），请求头touken字段"
    )
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
    @ApiOperation(value = "签到信息统计",notes =
            "参数：会议Id（路径变量），请求头touken字段。" +
                    "<br>返回对象数组，每个元素中字段：userId（用户ID）、userName（用户名）、signInTime（签到时间）"
    )
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
    @PostMapping("sign/{meetingId}/{code}")
    @ResponseBody
    @ApiOperation(value = "参会成员签到",notes =
            "参数：会议Id（meetingId，路径变量），随机参数（code，路径变量），登陆key，密码password"
    )
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

    @GetMapping("goSignPage")
    @ApiOperation(value = "二维码中请求的路径",notes =
            "这个接口仅为扫描二维码时跳转到指定页面，其中包含的参数将传递到静态页面中，参数的传递将使用模板引擎操作"
    )
    public ModelAndView signPage(
            Integer meetingId,
            String code){
        Map<String ,String > map = new HashMap<>();
        map.put("meetingId",meetingId.toString());
        map.put("code",code);
        System.out.println(map.toString());
        //实现方式 二维码链接到这个控制器中，跳转到下一个静态页面，携带
        return new ModelAndView("signPage","map",map);
    }
//    @RequestMapping("/static/{target}")
//    public String getStatic(@PathVariable String target){
//        return target;
//    }
//    @RequestMapping("/test1")
//    public String test(){
//        return "test";
//    }
}
