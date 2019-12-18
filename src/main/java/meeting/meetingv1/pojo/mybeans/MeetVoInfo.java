package meeting.meetingv1.pojo.mybeans;

public class MeetVoInfo {
    private Integer meetingId;
    private Integer taskId;
    private String taskinfo;
    private Integer workingtime;
    private Integer numbers;

    public MeetVoInfo() {
    }

    public MeetVoInfo(Integer meetingId, Integer taskId, String taskinfo, Integer workingtime, Integer numbers) {
        this.meetingId = meetingId;
        this.taskId = taskId;
        this.taskinfo = taskinfo;
        this.workingtime = workingtime;
        this.numbers = numbers;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskinfo() {
        return taskinfo;
    }

    public void setTaskinfo(String taskinfo) {
        this.taskinfo = taskinfo;
    }

    public Integer getWorkingtime() {
        return workingtime;
    }

    public void setWorkingtime(Integer workingtime) {
        this.workingtime = workingtime;
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }
}
