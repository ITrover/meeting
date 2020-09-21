package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.FileNotFindException1;
import meeting.meetingv1.exception.FileSendException;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.service.MeetFileSercice;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.service.UserService;
import meeting.meetingv1.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.List;

/**
 * 会议相关文档操作接口
 *
 * @author Lydia
 * @date 2020/9/21
 */
@Api(tags = {"会议相关文档操作接口"})
@RestController
public class FileUploadController {
    @Value("${user.avatar.path}")
    private String UPLOAD_PATH;
    @Value("${meeting.filePath.root}")
    private String meetingFileRootPath;
    @Value("${meeting.filePath.defultGuestImg}")
    private String defaultGuestImgPath;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;
    @Autowired
    MeetFileSercice meetFileSercice;
    @Autowired
    UserMeetingService userMeetingService;

    @PostMapping("/uploadimg")
    @ApiOperation(value = "上传用户头像",
            notes = "参数：" +
                    "1、用户的电话号 phone " +
                    "2、文件上传时name应为img " +
                    "3、登陆token <br>" +
                    "获取头像方式: 在上传图片后会将文件名以随机生成的uuid命名，文件名在用户字段的avatar中，直接访问服务器的80端口即可<br>" +
                    "如：172.0.0.1:80/1.png")
    public ResultBean upload(@ApiParam(name = "uploadFile",value = "用户头像文件")@RequestParam("img") MultipartFile uploadFile,
                             HttpServletRequest request,
                             String phone) {
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

    @ApiOperation(value = "获取用户头像",notes = "参数： <br>1、文件名")
    @GetMapping(value = "/userIcon/{filename}")
    public void getMeetIcon(@ApiParam(name = "filename",value = "文件名") @PathVariable String filename, HttpServletResponse response) throws IOException {

        OutputStream outputStream = response.getOutputStream();
        File file = new File(UPLOAD_PATH + "/" + filename);
        if (!file.exists()){
            file = new File(defaultGuestImgPath);
            response.setContentType("image/jpeg");
        }
        if (filename.contains("png")){
            response.setContentType("image/png");
        } else if (filename.contains("jpg") || filename.contains("jpeg")){
            response.setContentType("image/jpeg");
        }
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes,0,stream.available());
        response.getOutputStream().write(bytes);
        stream.close();
        outputStream.close();
    }

    @PostMapping("/file")
    @ApiOperation(value = "上传一个会议相关文件",
            notes = "参数： <br>" +
                    "1、会议ID meetingId  <br>" +
                    "2、文件上传时name应为file  <br>" +
                    "3、登陆token  <br>" +
                    " 注意：上传同名文件将覆盖旧文件")
    public ResultBean uploadMeetingFile(@ApiParam(name = "uploadFile",value = "会议相关文件") @RequestParam("file") MultipartFile uploadFile,String meetingId) throws UnsupportedEncodingException {
        if (uploadFile == null){
            return ResultBean.error(-7,"未选择文件");
        }
        File folder = getRootPath(meetingFileRootPath,meetingId);
        logger.info("会议相关文件夹路径:"+folder.getAbsolutePath());
        String originalFilename = URLDecoder.decode(uploadFile.getOriginalFilename(), "utf-8");
        //originalFilename
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

    /**
     * 获取会议相关文件
     * @param meetingId 会议Id
     * @return ResultBean
     */
    @GetMapping("/fileList")
    @ApiOperation(value = "获取会议相关的文件ID列表",
                  notes = "参数： <br>" +
                          "1、会议ID meetingId  <br> " +
                          "注意：返回的Json中data字段有会议ID：fileid;" +
                          "对应的会议id:meetingid;" +
                          "文件的物理路径：path;" +
                          "文件名：name;")
    public ResultBean getFileList(@ApiParam(name = "meetingId",value = "会议Id") Integer meetingId){
        Map<String, List> data = new HashMap<>(16);
        data.put("data",meetFileSercice.getFileInfoByMeetID(meetingId));
        return ResultBean.success(data);
    }

    @GetMapping("/file")
    @ApiOperation(value = "下载一个文件",notes = "参数： <br>1、会议ID meetingId<br>2、文件名fileName")
    public String downLoad(HttpServletResponse response,
                           @ApiParam(name = "meetingId",value = "会议Id") Integer meetingId,
                           @ApiParam(name = "fileName",value = "下载的文件名") String fileName) throws FileNotFindException1, FileSendException, IOException {
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
        if (bis != null){
            bis.close();
        }
        return null;
    }

    @DeleteMapping("/file")
    @ApiOperation(value = "删除一个会议相关文件",
                  notes = "参数： <br>" +
                          "1、会议ID meetingId<br>" +
                          "2、文件名 fileId <br>" +
                          "注意：只有会议创建者才有权限删除文件，否则返回对应错误信息")
    public ResultBean deleteFile(HttpServletRequest httpServletRequest,
                                 @ApiParam(name = "meetingId",value = "会议Id")Integer meetingId,
                                 @ApiParam(name = "fileId",value = "文件Id")Integer fileId){
        HttpSession session = httpServletRequest.getSession(false);
        Integer userId = (Integer)session.getAttribute("userId");
        List<UserMeeting> meetingsByBuilder = userMeetingService.getMeetingsByBuilder(userId);
        boolean flag = false;
        for (UserMeeting userMeeting :meetingsByBuilder){
            if (userMeeting.getMeetingid().equals(meetingId)){
                flag = true;
            }
        }
        if (!flag){
            return ResultBean.error(-12,"无权限");
        }
        meetFileSercice.deleteFile(fileId);
        return ResultBean.success();
    }

    /**
     * 获取文件根目录
     * @param meetingFileRootPath 会议文件根目录
     * @param meetingId 会议Id
     * @return 会议文件对象
     */
    private File getRootPath(String meetingFileRootPath,String meetingId){
        File file = new File(meetingFileRootPath+"/"+meetingId+"/");
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取根目录
     * @return 会议文件对象
     */
    private File getRootPath(){
        File file = new File(UPLOAD_PATH+"/");
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
}
