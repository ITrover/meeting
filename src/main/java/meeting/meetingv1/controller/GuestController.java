package meeting.meetingv1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import meeting.meetingv1.annotation.SysLog;
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

/**
 * 会议嘉宾操作接口
 *
 * @author Lydia
 * @date 2020/9/21
 */
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
    @ApiOperation(value = "增加嘉宾信息",
                  notes = "参数： <br>" +
                          "1、相关的会议id meetingId<br>" +
                          "2、嘉宾简介 introduction <br>" +
                          "3、嘉宾头像 icon （可选）<br>"+
                          "4、登陆token <br>" +
                          "5、嘉宾姓名name、职位position、航班信息flightInfo、身份证号personId、嘉宾电话guestTel"
                    )
    public ResultBean add(HttpServletRequest request,
                          @ApiParam(name = "meetingId",value = "会议Id") Integer meetingId,
                          @ApiParam(name = "introduction",value = "嘉宾简介") String introduction,
                          @ApiParam(name = "name",value = "嘉宾姓名") @Nullable String name,
                          @ApiParam(name = "position",value = "嘉宾职位") String position,
                          @ApiParam(name = "flightInfo",value = "航班信息") String flightInfo,
                          @ApiParam(name = "personId",value = "身份证号") String personId,
                          @ApiParam(name = "guestTel",value = "嘉宾电话") String guestTel,
                          @ApiParam(name = "icon",value = "嘉宾头像图片") @Nullable @RequestParam("icon") MultipartFile uploadFile) throws FileInfoStoreException {
        String newName = null;
        if (uploadFile != null){
            File folder = getRootPath(meetingFileRootPath,"guest");
            String originalFilename = uploadFile.getOriginalFilename();
            newName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            File file = new File(folder, newName);
            String PNG = "png";
            String JPG = "jpg";
            String JPEG = "jpeg";
            if (!originalFilename.contains(PNG) && originalFilename.contains(JPG) && originalFilename.contains(JPEG)){
                return ResultBean.error(-13,"文件只能为png或jpg");
            }
            try {
                uploadFile.transferTo(file);
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
                throw  new FileInfoStoreException();
            }
        }
        /**
         Integer guestid,
         Integer meetingid,
         String avatarUrl,
         String introduction,
         String name,
         String position,
         String flightInfo,
         String personId,
         String guestTel)
         **/
        Guest guest = new Guest(null,meetingId,newName,introduction,name,position,flightInfo,personId,guestTel);
        guestService.addGuest(guest);
        return ResultBean.success();
    }

    @ApiOperation(value = "获取嘉宾头像",notes = "参数： <br>1、嘉宾头像文件名 avatarUrl")
    @GetMapping(value = "/guestImg/{avatarUrl}")
    public void getMeetIcon(@ApiParam(name = "avatarUrl",value = "嘉宾头像文件名") @PathVariable String avatarUrl,
                            HttpServletResponse response) throws IOException {

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
    public ResultBean get(@ApiParam(name = "meetingId",value = "相关会议Id") @PathVariable Integer meetingId) {
        List<Guest> byGuestMeetingId = guestService.findByGuestMeetingId(meetingId);
        Map<String,List<Guest>> map = new HashMap<>(16);
        map.put("info",byGuestMeetingId);
        return ResultBean.success(map);
    }

    @ResponseBody
    @DeleteMapping("guest/{meetingId}")
    @ApiOperation(value = "会议发起者删除嘉宾",notes = "参数： <br>1、相关的会议id meetingId<br>")
    public ResultBean delete(@ApiParam(name = "meetingId",value = "相关会议Id") @PathVariable Integer meetingId,
                             @ApiParam(name = "guestId",value = "嘉宾Id") Integer guestId,
                             HttpServletRequest request) {
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
    public ResultBean update(HttpServletRequest request,
                             @ApiParam(name = "guestId",value = "嘉宾Id") Integer guestId,
                             @ApiParam(name = "meetingId",value = "会议Id") Integer meetingId,
                             @ApiParam(name = "introduction",value = "嘉宾简介") @Nullable String introduction,
                             @ApiParam(name = "name",value = "嘉宾姓名") @Nullable String name,
                             @ApiParam(name = "position",value = "嘉宾职位") @Nullable String position,
                             @ApiParam(name = "flightInfo",value = "航班信息") @Nullable String flightInfo,
                             @ApiParam(name = "personId",value = "嘉宾身份证号") @Nullable String personId,
                             @ApiParam(name = "guestTel",value = "嘉宾电话") @Nullable String guestTel,
                             @ApiParam(name = "icon",value = "嘉宾头像图片") @Nullable @RequestParam("icon") MultipartFile uploadFile) throws FileInfoStoreException {
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
        //如果文件夹不存在
        if(!file.exists()){
            //创建文件夹
            file.mkdirs();
        }
        return file;
    }
}
