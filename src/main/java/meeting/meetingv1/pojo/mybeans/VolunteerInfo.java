package meeting.meetingv1.pojo.mybeans;

public class VolunteerInfo {
    private Integer userId;
    private Integer meetingId;
    private Byte type;
    private Integer taskId;
    private String studentId;
    private String personId;
    private String taskInfo;

    public VolunteerInfo() {
    }


    public VolunteerInfo(Integer userId, Integer meetingId, Byte typeFlag, Integer taskId, String studentId, String personId, String taskInfo) {
        this.userId = userId;
        this.meetingId = meetingId;
        this.type = typeFlag;
        this.taskId = taskId;
        this.studentId = studentId;
        this.personId = personId;
        this.taskInfo = taskInfo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }
}
