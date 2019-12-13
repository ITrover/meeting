package meeting.meetingv1.MQ;


import meeting.meetingv1.pojo.Message;

public class Event {
    //操作标志数 1为正常单点消息 -1 为测试消息
    private int flag;
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
