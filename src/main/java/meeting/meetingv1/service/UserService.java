package meeting.meetingv1.service;

import meeting.meetingv1.exception.IncorrectCredentialsException;
import meeting.meetingv1.exception.SignUpColumnException;
import meeting.meetingv1.exception.UnknownAccountException;
import meeting.meetingv1.mapper.UserMapper;
import meeting.meetingv1.pojo.User;
import meeting.meetingv1.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 增加缓存
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = {"user"},key = "#userId")
    public User findUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 登陆验证，验证成功返回生成的JWT字符串
     * @param key
     * @param password
     * @return
     * @throws IncorrectCredentialsException
     * @throws UnknownAccountException
     */

    public String loginCheck(String key, String password) throws IncorrectCredentialsException, UnknownAccountException {
        boolean isMail = key.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        UserExample userExample = new UserExample();
        UserExample.Criteria userExampleCriteria = userExample.createCriteria();
        List<User> userList = null;
        if (isMail){
            userExampleCriteria.andEmailaddrEqualTo(key);
            userList = userMapper.selectByExample(userExample);
        }else{
            userExampleCriteria.andPhoneEqualTo(key);
            userList = userMapper.selectByExample(userExample);
        }
        if (userList.isEmpty()){
            throw new UnknownAccountException();
        }else if (password.equals(userList.get(0).getPassword())){
            return JwtService.getToken(userList.get(0));
        }else {
            throw new IncorrectCredentialsException();
        }
    }

    /**
     * 注册成功将Usermail对应的缓存删除
     * @param user
     * @return
     * @throws SignUpColumnException
     */
    public boolean signUp(User user) throws SignUpColumnException {

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw  new SignUpColumnException();
        }
        return true;
    }
    public boolean updatePwd(String mailAddr_or_Phone, String password) throws UnknownAccountException{
        boolean isMail = mailAddr_or_Phone.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        UserExample userExample = new UserExample();
        UserExample.Criteria userExampleCriteria = userExample.createCriteria();
        if (isMail){
            userExampleCriteria.andEmailaddrEqualTo(mailAddr_or_Phone);
        }else {
            userExampleCriteria.andPhoneEqualTo(mailAddr_or_Phone);
        }
        List<User> users = userMapper.selectByExample(userExample);
        if (users.isEmpty() || users == null){
            throw new UnknownAccountException();
        }
        users.get(0).setPassword(password);
        userMapper.updateByPrimaryKeySelective(users.get(0));
        return true;
    }

//    @Caching(
//            evict = {
//                    @CacheEvict(cacheNames = {"user"},key = "#user.phone")
//            }
//    )
@CachePut(value = "user",key = "#result.phone")
    public User updateUserInfo(User user){
        String key = user.getPhone()!=null ? user.getPhone():user.getEmailaddr();
        UserExample userExample = new UserExample();
        UserExample.Criteria userExampleCriteria = userExample.createCriteria();
        if (key.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            userExampleCriteria.andEmailaddrEqualTo(key);
        }else {
            userExampleCriteria.andPhoneEqualTo(key);
        }
        List<User> users = userMapper.selectByExample(userExample);
        if (user.getUsername() != null){
            users.get(0).setUsername(user.getUsername());
        }
        if (user.getRealname() != null){
            users.get(0).setRealname(user.getRealname());
        }
        if (user.getGender() != null){
            users.get(0).setGender(user.getGender());
        }
        if (user.getOrganization() != null){
            users.get(0).setOrganization(user.getOrganization());
        }
        userMapper.updateByPrimaryKeySelective(users.get(0));
        return users.get(0);
    }

    @Cacheable(cacheNames = {"user"},key = "#key")
    public User findUserByKey(String key) throws UnknownAccountException{
        UserExample userExample = new UserExample();
        UserExample.Criteria userExampleCriteria = userExample.createCriteria();
        if (key.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            userExampleCriteria.andEmailaddrEqualTo(key);
        }else {
            userExampleCriteria.andPhoneEqualTo(key);
        }
        List<User> users = null;
        try {
            users = userMapper.selectByExample(userExample);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownAccountException();
        }
        if (users.isEmpty()){
            throw new UnknownAccountException();
        }
        return users.get(0);
    }
//    public
}
