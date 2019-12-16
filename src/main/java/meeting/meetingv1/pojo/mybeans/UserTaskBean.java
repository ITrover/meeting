package meeting.meetingv1.pojo.mybeans;

import meeting.meetingv1.pojo.Voluntask;
import meeting.meetingv1.pojo.Voluntinfo;

import java.util.List;

//志愿者信息的集成类
public class UserTaskBean {
    private Voluntinfo voluntinfo;
    private Voluntask myTask;


    public UserTaskBean(Voluntinfo voluntinfo,Voluntask myTask ) {
        this.myTask = myTask;
        this.voluntinfo = voluntinfo;
    }

    public UserTaskBean() {
    }

    public Voluntask getMyTask() {
        return myTask;
    }

    public void setMyTask(Voluntask myTask) {
        this.myTask = myTask;
    }

    public Voluntinfo getVoluntinfo() {
        return voluntinfo;
    }

    public void setVoluntinfo(Voluntinfo voluntinfo) {
        this.voluntinfo = voluntinfo;
    }
}
