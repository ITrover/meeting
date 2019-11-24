package meeting.meetingv1.controller;

import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.service.UserService;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUploadController {
    @Value("${user.avatar.path}")
    private String UPLOAD_PATH;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;

    @UserLoginToken
    @PostMapping("/uploadimg")
    public ResultBean upload(@RequestParam("img")MultipartFile uploadFile, HttpServletRequest request, String phone) {
        if (uploadFile == null){
            return ResultBean.error(-7,"未选择文件");
        }
        File folder = getRootPath(phone);
        logger.info("文件夹路径:"+folder.getAbsolutePath());
        String originalFilename = uploadFile.getOriginalFilename();
        logger.info("文件原名："+originalFilename);
        String newName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
        File file = new File(folder, newName);

        try {
            uploadFile.transferTo(file);
            String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + UPLOAD_PATH +"/" + newName;
            logger.info("文件访问路径："+filePath);
            User user = new User();
            user.setPhone(phone);
            logger.info(newName);
            user.setAvatar(newName);
            userService.updateUserInfo(user);
            return ResultBean.success();
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
            return ResultBean.error(-8,"文件保存失败");
        }
    }

    private File getRootPath(String phone){
        File file = new File(UPLOAD_PATH+"/");
        if(!file.exists()){//如果文件夹不存在
            file.mkdirs();//创建文件夹
        }
        return file;
    }
}
