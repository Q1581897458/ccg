package com.mr.service;

import com.mr.bo.UserInfo;
import com.mr.client.UserClient;
import com.mr.config.JwtConfig;
import com.mr.pojo.User;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtConfig jwtConfig;

    public String auth(String username, String password) {
//校验账号密码是否正确 通过fegin
           User user = userClient.queryList(username,password);

           if (user==null){
               //账号密码错误
               return null;
           }
               //账号密码正确
               UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());
               try {
                   String token = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getExpire());
             return token;
               } catch (Exception e) {
                   e.printStackTrace();
                   return null;
               }




    }
}
