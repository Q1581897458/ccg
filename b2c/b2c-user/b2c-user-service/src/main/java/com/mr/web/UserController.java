package com.mr.web;

import com.mr.pojo.User;
import com.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> cjeck(
            @PathVariable(value = "data") String data,
            @PathVariable(value = "type") Integer type
    ){

        if (data == null || type ==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean resu = userService.validNameAndPhone(data, type);
        return ResponseEntity.ok(resu);
    }


    @PostMapping("register")
    public ResponseEntity<Void> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone
    ){


       if (username == null ||password == null||phone==null){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

       try {
           userService.register(username,password,phone);
       }catch (Exception e){
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }

        return new  ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("query")
    public ResponseEntity<User> queryList(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ){

        if (username == null ||password == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            User user=  userService.query(username,password);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }


}
