package meeting.meetingv1.MQ;

public class SendToMany {
    private int meetingId;
    private String content;
    private Integer type;
    public SendToMany() {
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public SendToMany(int meetingId, String content, Integer type) {
        this.meetingId = meetingId;
        this.content = content;
        this.type = type;
    }
}
