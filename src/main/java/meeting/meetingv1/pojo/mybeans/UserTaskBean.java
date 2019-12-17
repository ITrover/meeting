package meeting.meetingv1.pojo.mybeans;

import meeting.meetingv1.pojo.Voluntask;
import meeting.meetingv1.pojo.Voluntinfo;

import java.util.List;

//志愿者信息的集成类
public class UserTaskBean {
    private Integer typeFlag;
    private Voluntinfo voluntinfo;
    private Voluntask myTask;

    public Integer getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(Integer typeFlag) {
        this.typeFlag = typeFlag;
    }

    public Voluntinfo getVoluntinfo() {
        return voluntinfo;
    }

    public void setVoluntinfo(Voluntinfo voluntinfo) {
        this.voluntinfo = voluntinfo;
    }

    public Voluntask getMyTask() {
        return myTask;
    }

    public void setMyTask(Voluntask myTask) {
        this.myTask = myTask;
    }

    public UserTaskBean() {
    }

    public UserTaskBean(Integer typeFlag, Voluntinfo voluntinfo, Voluntask myTask) {
        this.typeFlag = typeFlag;
        this.voluntinfo = voluntinfo;
        this.myTask = myTask;
    }
}
