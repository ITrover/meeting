package meeting.meetingv1.pojo.mybeans;

import meeting.meetingv1.pojo.User;

public class UserPublicInfos {
    private Integer userId;
    private String userName;
    private String phone;
    private String email;
    public UserPublicInfos(User user) {
        this.userId = user.getUserid();
        this.userName = user.getUsername();
        this.phone = user.getPhone();
        this.email = user.getEmailaddr();
    }

    public UserPublicInfos(Integer userId, String userName, String phone, String email) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserPublicInfos{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
