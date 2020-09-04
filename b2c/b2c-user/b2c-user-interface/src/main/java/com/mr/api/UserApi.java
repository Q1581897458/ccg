package com.mr.api;

import com.mr.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {


    @GetMapping("check/{data}/{type}")
    public Boolean cjeck(
            @PathVariable(value = "data") String data,
            @PathVariable(value = "type") Integer type
    );


    @PostMapping("register")
    public void register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone
    );

    @GetMapping("query")
    public User queryList(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    );




}
