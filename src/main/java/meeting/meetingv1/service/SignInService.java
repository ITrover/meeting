package meeting.meetingv1.service;

import com.google.zxing.WriterException;
import meeting.meetingv1.mapper.MeetingSignInTableMapper;
import meeting.meetingv1.mapper.MeetingSignInUserMapper;
import meeting.meetingv1.pojo.MeetingSignInTable;
import meeting.meetingv1.pojo.MeetingSignInUser;
import meeting.meetingv1.pojo.MeetingSignInUserExample;
import meeting.meetingv1.pojo.UserMeeting;
import meeting.meetingv1.util.Md5Util;
import meeting.meetingv1.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import javax.xml.ws.soap.Addressing;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class SignInService {
    @Autowired
    MeetingSignInTableMapper tableMapper;
    @Autowired
    MeetingSignInUserMapper signInUserMapper;
    @Autowired
    QRCodeGenerator qrCodeGenerator;
    @Value(value = "${app.base.url}")
    String baseURL;
    //用户生成签到二维码
    public void getSignPic(OutputStream outputStream,Integer meetingId) throws IOException, WriterException {
        MeetingSignInTable signInTable = tableMapper.selectByPrimaryKey(meetingId);
        qrCodeGenerator.generateQRCodeImage(baseURL+"/goSignPage/"+meetingId+"/"+signInTable.getSpecialCode(),400,400,outputStream);
    }
    //增加签到任务
    public void addSign(Date signInBeginTime,Integer meetingId){
        String hash = Md5Util.generateHash(meetingId + ""+ new Date().getTime());
        MeetingSignInTable table = new MeetingSignInTable(meetingId,signInBeginTime,hash);
        try{
            tableMapper.insert(table);
        }catch (DuplicateKeyException e){
            tableMapper.updateByPrimaryKey(table);
        }
    }
    //删除任务
    public void deleteSign(Integer meetingId){
        tableMapper.deleteByPrimaryKey(meetingId);
    }
    //验证签到的有效性 时间+code
    public boolean signInAvailable(Integer meetingId,String code){
        MeetingSignInTable table;
        try {
            table = tableMapper.selectByPrimaryKey(meetingId);
        }catch (Exception e){
            return false;
        }
        if (table == null){
            return false;
        }
        if (table.getSpecialCode().equals(code) && table.getSignInStartTime().before(new Date())){
            return true;
        }
        return false;
    }
///获取已经签到的用户
    public List<MeetingSignInUser> meetingSignedList(Integer meetingId){
        MeetingSignInUserExample example = new MeetingSignInUserExample();
        MeetingSignInUserExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingId);
        List<MeetingSignInUser> signInUsers = signInUserMapper.selectByExample(example);
        return signInUsers;
    }
    //查看签到统计信息
    @Autowired
    UserMeetingService userMeetingService;
    @Autowired
    UserService userService;
    public Collection SignStatistic(Integer meetingId){
        Byte type = 2;//2 为参加的会议，应该整一个静态变量的
        List<UserMeeting> joinedUsers = userMeetingService.findPreferenceByMeet(meetingId, type);
        List<MeetingSignInUser> signList = meetingSignedList(meetingId);
        Map<Integer,MeetingSignInUser> signedMap = new HashMap<>();
        for (MeetingSignInUser user : signList)
            signedMap.put(user.getUserId(),user);
        Map<Integer,SignInfo> infoMap = new HashMap<>();
        for (UserMeeting joinedUser : joinedUsers) {
            infoMap.put(joinedUser.getUserid(),
                    new SignInfo(
                            joinedUser.getUserid(),
                            userService.findUserById(joinedUser.getUserid()).getUsername(),
                            signedMap.get(joinedUser.getUserid()) == null ? null:signedMap.get(joinedUser.getUserid()).getSignTime()
                            ));
        }
        return infoMap.values();
    }
    ////////////////////////////////////////////////////////////////////////
    //用户签到模块
    public boolean doSign(Integer userId,Integer meetingId){
        MeetingSignInUser signInUser = new MeetingSignInUser(meetingId,userId,new Date());
        try{
            signInUserMapper.insert(signInUser);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    private class SignInfo{
        private Integer userId;
        private String  userName;
        private Date    signInTime;

        public SignInfo(Integer userId, String userName, Date signInTime) {
            this.userId = userId;
            this.userName = userName;
            this.signInTime = signInTime;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Date getSignInTime() {
            return signInTime;
        }

        public void setSignInTime(Date signInTime) {
            this.signInTime = signInTime;
        }
    }
}
