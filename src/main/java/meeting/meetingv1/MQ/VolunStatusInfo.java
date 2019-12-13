package meeting.meetingv1.MQ;

public class VolunStatusInfo {
    private int meetingId;
    private int userId;
    private int type;

    public VolunStatusInfo() {
    }

    public VolunStatusInfo(int meetingId, int userId, int type) {
        this.meetingId = meetingId;
        this.userId = userId;
        this.type = type;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public VolunStatusInfo setMeetingId(int meetingId) {
        this.meetingId = meetingId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public VolunStatusInfo setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getType() {
        return type;
    }

    public VolunStatusInfo setType(int type) {
        this.type = type;
        return this;
    }
}
