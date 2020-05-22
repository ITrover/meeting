package meeting.meetingv1.service;

import meeting.meetingv1.mapper.GuestMapper;
import meeting.meetingv1.mapper.RoomMapper;
import meeting.meetingv1.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    GuestMapper guestMapper;
    public void addRoom(Room room){
        room.setGuestId(null);
        roomMapper.insert(room);
    }
    public void deleteRoom(RoomKey roomKey){
        roomMapper.deleteByPrimaryKey(roomKey);
    }
    //分配房间
    public void assignRoomForGuest(Integer roomId,Integer meeingId,Integer guestID){
        if (roomId != null && guestID != null){
            Room room = roomMapper.selectByPrimaryKey(new RoomKey(roomId, meeingId));
            room.setGuestId(guestID);
            roomMapper.updateByPrimaryKey(room);
        }
    }
    //查看分配房间详情
    public Map getAssignInfos(Integer meetingID){
        RoomExample example = new RoomExample();
        RoomExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingID);
        criteria.andGuestIdNotEqualTo(-1);
        List<Room> rooms = roomMapper.selectByExample(example);
        Map<Room,Guest> map = new HashMap<>();
        for (Room room:rooms) {
            Guest guest = guestMapper.selectByPrimaryKey(new GuestKey(room.getGuestId(), meetingID));
            map.put(room,guest);
        }
        return map;
    }
    //查看可用房型信息
    public List getAllSuitableRoom(Integer meetingID){
        RoomExample example = new RoomExample();
        RoomExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingID);
        criteria.andGuestIdEqualTo(-1);
        List<Room> rooms = roomMapper.selectByExample(example);
        return rooms;
    }
    //查看所有房间
    public List allRooms(Integer meetingID){
        RoomExample example = new RoomExample();
        RoomExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingID);
        List<Room> rooms = roomMapper.selectByExample(example);
        return rooms;
    }
     //查看可用房型统计
    public Map statistic(Integer meetingID){
        RoomExample example = new RoomExample();
        RoomExample.Criteria criteria = example.createCriteria();
        criteria.andMeetingIdEqualTo(meetingID);
        criteria.andGuestIdEqualTo(-1);
        List<Room> rooms = roomMapper.selectByExample(example);
        Map<String,Integer> map = new HashMap<>();
        for (Room room :rooms){
            if (map.containsKey(room.getRoomType())){
                Integer integer = map.get(room.getRoomType());
                map.put(room.getRoomType(),integer++);
            }else {
                map.put(room.getRoomType(),1);
            }
        }
        return map;
    }
}
