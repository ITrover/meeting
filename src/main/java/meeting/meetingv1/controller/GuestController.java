package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meeting.meetingv1.annotation.UserLoginToken;
import meeting.meetingv1.exception.FileInfoStoreException;
import meeting.meetingv1.pojo.Guest;
import meeting.meetingv1.service.GuestService;
import meeting.meetingv1.service.UserMeetingService;
import meeting.meetingv1.util.Check;
import meeting.meetingv1.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@Api(tags = "会议嘉宾操作接口")
public class GuestController {

    @Value("${meeting.filePath.root}")
    private String meetingFileRootPath;
    @Value("${meeting.filePath.defultGuestImg}")
    private String defaultGuestImgPath;
    @Autowired
    GuestService guestService;
    @Autowired
    UserMeetingService userMeetingService;

    @ResponseBody
    @PostMapping("guest")
    @ApiOperation(value = "增加嘉宾信息",notes =
            "参数： <br>1、相关的会议id meetingId<br>" +
                       "<br>2、嘉宾简介 introduction <br>" +
                        "<br>3、嘉宾头像 icon （可选）"+
                       "<br>4、登陆token" +
                    "<br>嘉宾姓名name、职位position、航班信息flightInfo、身份证号personId、嘉宾电话guestTel"
                    )
    @UserLoginToken
    public ResultBean add(HttpServletRequest request,Integer meetingId,String introduction,
                          @Nullable String name, String position, String flightInfo, String personId,String guestTel
    ,@Nullable @RequestParam("icon") MultipartFile uploadFile) throws FileInfoStoreException {
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        String newName = null;
        if (uploadFile != null){
            File folder = getRootPath(meetingFileRootPath,"guest");
            String originalFilename = uploadFile.getOriginalFilename();
            newName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            File file = new File(folder, newName);
            if (!originalFilename.contains("png") && originalFilename.contains("jpg") && originalFilename.contains("jpeg")){
                return ResultBean.error(-13,"文件只能为png或jpg");
            }
            try {
                uploadFile.transferTo(file);
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
                throw  new FileInfoStoreException();
            }
        }/**
         Integer guestid,
         Integer meetingid,
         String avatarUrl,
         String introduction,
         String name,
         String position,
         String flightInfo,
         String personId,
         String guestTel) {

         */
        Guest guest = new Guest(null,meetingId,newName,introduction,name,position,flightInfo,personId,guestTel);
        guestService.addGuest(guest);
        return ResultBean.success();
    }

    @ApiOperation(value = "获取嘉宾头像",notes = "参数： <br>1、嘉宾头像文件名 avatarUrl")
    @GetMapping(value = "/guestImg/{avatarUrl}")
    public void getMeetIcon(@PathVariable String avatarUrl, HttpServletResponse response) throws IOException {

        OutputStream outputStream = response.getOutputStream();
        File file = new File(meetingFileRootPath +"/guest/" + avatarUrl);
        if (!file.exists()){
            file = new File(defaultGuestImgPath);

            response.setContentType("image/jpeg");
        }
        if (avatarUrl.contains("png")){
            response.setContentType("image/png");
        } else if (avatarUrl.contains("jpg") || avatarUrl.contains("jpeg")){
            response.setContentType("image/jpeg");
        }
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes,0,stream.available());
        response.getOutputStream().write(bytes);
    }

    @ResponseBody
    @GetMapping("guest/{meetingId}")
    @ApiOperation(value = "获取嘉宾信息(文本)",notes = "参数： <br>1、相关的会议id meetingId<br>")
    public ResultBean get(@PathVariable Integer meetingId) {
        List<Guest> byGuestMeetingId = guestService.findByGuestMeetingId(meetingId);
        Map<String,List<Guest>> map = new HashMap<>();
        map.put("info",byGuestMeetingId);
        return ResultBean.success(map);
    }

    @ResponseBody
    @UserLoginToken
    @DeleteMapping("guest/{meetingId}")
    @ApiOperation(value = "会议发起者删除嘉宾",notes = "参数： <br>1、相关的会议id meetingId<br>")
    public ResultBean delete(@PathVariable Integer meetingId,Integer guestId,HttpServletRequest request) {
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        Guest guest = new Guest();
        guest.setGuestid(guestId);
        guest.setMeetingid(meetingId);
        guestService.deleteGuest(guest);
        return ResultBean.success();
    }

    @ResponseBody
    @PostMapping("updateGuest")
    @ApiOperation(value = "修改嘉宾信息",notes =
            "参数： <br>1、相关的会议id meetingId<br>" +
                    "2、嘉宾简介 introduction（可选） <br>" +
                    "3、嘉宾头像(图片) icon （可选）"+
                    "4、登陆token<br>" +
                    "5、嘉宾ID guestId" +
                    "6、嘉宾姓名 name" +
                    "7、嘉宾电话 guestTel"
    )
    @UserLoginToken
    public ResultBean update(HttpServletRequest request,Integer guestId,Integer meetingId,
                             @Nullable String introduction,
                             @Nullable String name,
                             @Nullable String position,
                             @Nullable String flightInfo,
                             @Nullable String personId,
                             @Nullable String guestTel
            ,@Nullable @RequestParam("icon") MultipartFile uploadFile) throws FileInfoStoreException {
        if (!Check.checkUp(request,userMeetingService,meetingId)){
            return ResultBean.error(-12,"无权限");
        }
        String newName = null;
        if (uploadFile != null){
            File folder = getRootPath(meetingFileRootPath,meetingId+"/guest");
            String originalFilename = uploadFile.getOriginalFilename();
            newName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            File file = new File(folder, newName);

            try {
                uploadFile.transferTo(file);
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
                throw  new FileInfoStoreException();
            }
        }
        Guest guest = new Guest(guestId,meetingId,newName,introduction,name,position,flightInfo,personId,guestTel);
        guestService.updateGuest(guest);
        return ResultBean.success();
    }

    private File getRootPath(String meetingFileRootPath,String meetingId){
        File file = new File(meetingFileRootPath+"/"+meetingId+"/");
        if(!file.exists()){//如果文件夹不存在
            file.mkdirs();//创建文件夹
        }
        return file;
    }
}
