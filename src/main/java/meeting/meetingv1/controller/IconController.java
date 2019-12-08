package meeting.meetingv1.controller;

import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
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
import java.util.List;

@Controller
public class IconController {
    @Value("${meeting.filePath.root}")
    private String meetingFileRootPath;
    @Value("${meeting.filePath.defaultIconPath}")
    private String defaultIconPath;
    @Autowired
    UserMeetingService userMeetingService;

    @GetMapping(value = "/meetingIcon/{meetingId}",produces = MediaType.IMAGE_PNG_VALUE)
    public void getMeetIcon(@PathVariable String meetingId, HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        File file = new File(meetingFileRootPath + "/" + meetingId + "/" + "icon.png");
        if (!file.exists()){
            file = new File(defaultIconPath);
        }
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes,0,stream.available());
        response.getOutputStream().write(bytes);
    }

    @PostMapping("/meetingIcon/{meetingId}")
    @ResponseBody
    public ResultBean updateIcon(@RequestParam("icon") MultipartFile uploadFile, HttpServletRequest httpServletRequest,@PathVariable Integer meetingId) throws IOException {
        if (!Check.checkUp(httpServletRequest,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        uploadFile.transferTo(new File(meetingFileRootPath + "/" + meetingId + "/" + "icon.png"));
        return ResultBean.success();
    }
}
