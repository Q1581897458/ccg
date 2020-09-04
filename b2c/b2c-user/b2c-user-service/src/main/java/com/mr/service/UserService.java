package com.mr.service;

import com.mr.mapper.UserMapper;
import com.mr.pojo.User;
import com.mr.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean validNameAndPhone(String data , Integer type){

        User user = new User();

        if (type ==1 ){
            user.setUsername(data);
        }else if (type ==2){
            user.setPhone(data);
        }

        return userMapper.selectCount(user) == 0;
    }


    public Boolean register(String username, String password, String phone) {

        User user = new User();
        //注册时间
        user.setCreated(new Date());
        user.setUsername(username);
        user.setPhone(phone);
        //设置uuid
        user.setSalt(MD5Utils.generateSalt());
        //密码加密
        user.setPassword(MD5Utils.md5Hex(password,user.getSalt()));
        //如果返回结果1 为成功，0为失败
        return userMapper.insert(user)==1;
    }

    public User query(String username, String password) {

        User ex = new User();
        ex.setUsername(username);

        User user = userMapper.selectOne(ex);

        if (user == null){
            return null;
        }else if (!user.getPassword().equals(MD5Utils.md5Hex(password,user.getSalt()))){
            return null;
        }

        return user;
    }

  /*  public boolean register(String username, String password, String phone) {
    }*/
}
