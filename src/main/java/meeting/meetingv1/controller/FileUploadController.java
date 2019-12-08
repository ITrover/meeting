package meeting.meetingv1.controller;

import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.FileNotFindException1;
import meeting.meetingv1.exception.FileSendException;
import meeting.meetingv1.pojo.Meeting;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.MeetFileSercice;
import meeting.meetingv1.service.MeetingService;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.service.UserService;
import meeting.meetingv1.util.ResultBean;
import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@RestController
public class FileUploadController {
    @Value("${user.avatar.path}")
    private String UPLOAD_PATH;
    @Value("${meeting.filePath.root}")
    private String meetingFileRootPath;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;
    @Autowired
    MeetFileSercice meetFileSercice;
    @Autowired
    UserMeetingService userMeetingService;

//    @UserLoginToken
    @PostMapping("/uploadimg")
    public ResultBean upload(@RequestParam("img")MultipartFile uploadFile, HttpServletRequest request, String phone) {
        if (uploadFile == null){
            return ResultBean.error(-7,"未选择文件");
        }
        File folder = getRootPath();
        logger.info("头像文件夹路径:"+folder.getAbsolutePath());
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
    @PostMapping("/file")
    public ResultBean uploadMeetingFile(@RequestParam("file")MultipartFile uploadFile,String meetingId){
        if (uploadFile == null){
            return ResultBean.error(-7,"未选择文件");
        }
        File folder = getRootPath(meetingFileRootPath,meetingId);
        logger.info("会议相关文件夹路径:"+folder.getAbsolutePath());
        String originalFilename = uploadFile.getOriginalFilename();
        logger.info("文件原名："+originalFilename);
        File file = new File(folder, originalFilename);
        try{
            if (file.exists()){
                file.delete();
            }
            uploadFile.transferTo(file);
            meetFileSercice.addFile(originalFilename,file.getPath(),new Integer(meetingId));
        }catch (Exception e){
            e.printStackTrace();
            file.delete();
            return ResultBean.error(-8,"文件保存失败");
        }
        return  ResultBean.success();
    }
    @GetMapping("/fileList")
    public ResultBean getFileList(Integer meetingId){
        Map<String, List> data = new HashMap<>();
        data.put("data",meetFileSercice.getFileInfoByMeetID(meetingId));
        return ResultBean.success(data);
    }
    @GetMapping("/file")
    public String downLoad(HttpServletResponse response,Integer meetingId,String fileName) throws FileNotFindException1, FileSendException {
        File file = new File(meetingFileRootPath+"/"+meetingId+"/"+fileName);
        if (!file.exists()){
            throw new FileNotFindException1();
        }
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        byte[] buffer = new byte[2048];
        BufferedInputStream bis = null;
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileSendException();
        }
        return null;
    }


    @UserLoginToken
    @DeleteMapping("/file")
    public ResultBean deleteFile(HttpServletRequest httpServletRequest,Integer meetingId,Integer fileId){
        HttpSession session = httpServletRequest.getSession(false);
        Integer userId = (Integer)session.getAttribute("userId");
        List<UserMeeting> meetingsByBuilder = userMeetingService.getMeetingsByBuilder(userId);
        boolean flag = false;
        for (UserMeeting userMeeting :meetingsByBuilder){
            if (userMeeting.getUserid().equals(meetingId)){
                flag = true;
            }
        }
        if (!flag){
            return ResultBean.error(-12,"无权限");
        }
        meetFileSercice.deleteFile(fileId);
        return ResultBean.success();
    }
//    public
    private File getRootPath(String meetingFileRootPath,String meetingId){
        File file = new File(meetingFileRootPath+"/"+meetingId+"/");
        if(!file.exists()){//如果文件夹不存在
            file.mkdirs();//创建文件夹
        }
        return file;
    }
    private File getRootPath(){
        File file = new File(UPLOAD_PATH+"/");
        if(!file.exists()){//如果文件夹不存在
            file.mkdirs();//创建文件夹
        }
        return file;
    }
}
