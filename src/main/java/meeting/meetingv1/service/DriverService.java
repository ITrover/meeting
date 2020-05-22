package meeting.meetingv1.service;

import meeting.meetingv1.mapper.DriverGuestArengementMapper;
import meeting.meetingv1.mapper.DriverMapper;
import meeting.meetingv1.mapper.GuestMapper;
import meeting.meetingv1.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverService {
    @Autowired
    DriverMapper driverMapper;

    @Autowired
    DriverGuestArengementMapper driverGuestArengementMapper;
    @Autowired
    GuestMapper guestMapper;
    //查看会议分配的司机详情，先查询会议司机中已经被分配的司机list，根据其中的司机ID去分配表中查询嘉宾信息
    public Map<Driver,Guest> assignedDriverInfos(Integer meetingId){
        DriverExample example = new DriverExample();
        DriverExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingId);
        criteria.andIsArrengeEqualTo(0);//查找已分配的司机
        List<Driver> drivers = driverMapper.selectByExample(example);
        Map<Driver,Guest> map = new HashMap<>();
        for (Driver d:drivers) {
            DriverGuestArengementExample example1 = new DriverGuestArengementExample();
            DriverGuestArengementExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andDriverIdEqualTo(d.getDriverId());
            List<DriverGuestArengement> list = driverGuestArengementMapper.selectByExample(example1);
            Guest guest = guestMapper.selectByPrimaryKey(new GuestKey(list.get(0).getGuestId(), meetingId));
            map.put(d,guest);
        }
        return map;
    }
    public void addDriver(Driver driver){
        driverMapper.insert(driver);
    }
    public void deleteDriver(DriverKey driverKey){
        driverMapper.deleteByPrimaryKey(driverKey);
    }
    List getAllDriverByMeeting(Integer meetingId){
        DriverExample driverExample = new DriverExample();
        DriverExample.Criteria criteria = driverExample.createCriteria();
        criteria.andMeetingIdEqualTo(meetingId);
        List<Driver> drivers = driverMapper.selectByExample(driverExample);
        return drivers;
    }
    //查看所有可用司机
    public List assignableDrivers(Integer meetingId){
        DriverExample example = new DriverExample();
        DriverExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingId);
        criteria.andIsArrengeEqualTo(1);
        List<Driver> drivers = driverMapper.selectByExample(example);
        return drivers;
    }
    //查看所有可用司机
    public List allDrivers(Integer meetingId){
        DriverExample example = new DriverExample();
        DriverExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingId);
        List<Driver> drivers = driverMapper.selectByExample(example);
        return drivers;
    }
    //分配司机
    public void assignDriverForGuest(Integer driverID,Integer guestID){
        if (driverID != null && guestID != null){
            driverGuestArengementMapper.insert(new DriverGuestArengement(null,driverID,guestID));
            driverMapper.assignDriverFlagByPrimaryKey(new DriverKey(driverID,guestID));
        }
    }
    //查看司机统计
    public Map statistic(Integer meetingID){
        DriverExample example = new DriverExample();
        DriverExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingID);
        criteria.andIsArrengeEqualTo(1);
        List<Driver> drivers = driverMapper.selectByExample(example);
        Map<String,Integer> map = new HashMap<>();
        for (Driver driver :drivers){
            if (map.containsKey(driver.getCarType())){
                Integer integer = map.get(driver.getCarType());
                map.put(driver.getCarType(),integer++);
            }else {
                map.put(driver.getCarType(),1);
            }
        }
        return map;
    }
}
