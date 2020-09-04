package com.mr.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @GetMapping("login")
    public String Login(String username, String password , HttpSession session){
        if (username.equals("aaa") && password.equals("aaaa")){
            session.setAttribute("admin_user",username);
            return "ok";
        }

        return  "nu";
    }

    //获取session
    @GetMapping
    public String show(HttpSession session){

        //显示当前用户等订单
        if (session.getAttribute("adminuser")!=null){
            return "只是 " +session.getAttribute("admin_user")+"订单界面";
        }else {
            return "无用户登录 " +session.getAttribute("admin_user")+"订单界面";
        }

    }

}
