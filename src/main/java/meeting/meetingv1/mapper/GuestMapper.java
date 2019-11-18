package meeting.meetingv1.mapper;

import meeting.meetingv1.pojo.Guest;
import meeting.meetingv1.pojo.GuestExample;
import meeting.meetingv1.pojo.GuestKey;

import java.util.List;

public interface GuestMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table guest
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(GuestKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table guest
     *
     * @mbg.generated
     */
    int insert(Guest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table guest
     *
     * @mbg.generated
     */
    int insertSelective(Guest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table guest
     *
     * @mbg.generated
     */
    List<Guest> selectByExample(GuestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table guest
     *
     * @mbg.generated
     */
    Guest selectByPrimaryKey(GuestKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table guest
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Guest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table guest
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Guest record);
}