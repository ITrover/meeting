package meeting.meetingv1.mapper;

import meeting.meetingv1.pojo.Meetingfile;
import meeting.meetingv1.pojo.MeetingfileExample;

import java.util.List;

public interface MeetingfileMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meetingfile
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer fileid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meetingfile
     *
     * @mbg.generated
     */
    int insert(Meetingfile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meetingfile
     *
     * @mbg.generated
     */
    int insertSelective(Meetingfile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meetingfile
     *
     * @mbg.generated
     */
    List<Meetingfile> selectByExample(MeetingfileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meetingfile
     *
     * @mbg.generated
     */
    Meetingfile selectByPrimaryKey(Integer fileid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meetingfile
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Meetingfile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meetingfile
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Meetingfile record);

    List<Meetingfile> selectByMeetingId(Integer meetingId);
}