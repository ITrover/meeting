package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = {"会议主题图操作接口"})
public class IconController {

    @Value("${meeting.filePath.root}")
    private String meetingFileRootPath;
    @Value("${meeting.filePath.defaultIconPath}")
    private String defaultIconPath;
    @Autowired
    UserMeetingService userMeetingService;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @ResponseBody
    @ApiOperation(value = "获取会议主题图",notes = "参数： <br>1、会议ID meetingId<br>注意：<br>   " +
            "1、Resful风格接口，会议id为路径变量，比如会议1的路径为：.../meetingIcon/1 <br>   2、未上传过会议主题图返回默认图")
    @GetMapping(value = "/meetingIcon/{meetingId}",produces = "applications/json;charset=UTF-8")
    public ResultBean getMeetingIcon(@PathVariable String meetingId, HttpServletResponse response) throws IOException {
        //OutputStream outputStream = response.getOutputStream();
        File file = new File(meetingFileRootPath + "/" + meetingId + "/" + "icon.png");
        if (!file.exists()){
            file = new File(defaultIconPath);
        }
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes,0,stream.available());
        try{
            response.getOutputStream().write(bytes);
        }catch (Exception e){
            stream.close();
        }

        stream.close();

        Map<String,Object> map=new HashMap<>();
        String url = meetingFileRootPath + "/" + 54 + "/" + "icon.png";
        map.put("url",url);
        logger.error("map  : "+map.toString());
        return ResultBean.success(map);


    }

    @PostMapping("/meetingIcon/{meetingId}")
    @ResponseBody
    @UserLoginToken
    @ApiOperation(value = "上传会议主题图",notes = "参数： <br>1、文件上传时name为icon<br>2、会议ID meetingId <br>3、登陆token<br>注意：会议创建者可以修改")
    public ResultBean updateIcon(@RequestParam("icon") MultipartFile uploadFile, HttpServletRequest httpServletRequest,@PathVariable Integer meetingId) throws IOException {
        logger.info("开始上传会议图片");
        if (!Check.checkUp(httpServletRequest,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        String fileRealPath = meetingFileRootPath + "/" + meetingId + "/";
        File file = new File(meetingFileRootPath + "/" + meetingId+"/" );
        if (!file.exists()){
            file.mkdir();
        }
        File file2 = new File(meetingFileRootPath + "/" + meetingId + "/" + "icon.png");
        uploadFile.transferTo(file2);
        logger.info("会议图片上传成功！会议id:"+meetingId+" 存储路径："+fileRealPath+ "icon.png");
        Map<String,Object> map=new HashMap<>();
        String url = meetingFileRootPath + "/" + meetingId + "/" + "icon.png";
        map.put("url",url);
        return ResultBean.success(map);
    }
}
