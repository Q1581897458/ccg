package com.mr.web;

import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.service.AuthService;
import com.mr.common.utils.CookieUtils;
import com.mr.util.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JwtConfig jwtConfig;


    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam(value="username") String username,
            @RequestParam(value = "password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        //验证账号是否存在 是否正确
        String token=service.auth(username,password);
    //成功 生产token 保存用户到浏览器中的 cookie中
        if (StringUtils.isEmpty(token)){
            //4001 错
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.out.println(token);

        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),token,jwtConfig.getCookieMaxAge(),true);
        return ResponseEntity.ok(null);
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("B2C_TOKEN") String token,
                                               HttpServletRequest request,
                                               HttpServletResponse response){

        try {

           UserInfo aa= JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());

            // 可以解析token的证明用户是正确登陆状态，重新生成token，这样登陆状态就又刷新30分钟过期了
            String newToken = JwtUtils.generateToken(aa,
                    jwtConfig.getPrivateKey(), jwtConfig.getExpire());


            // 将新token写入cookie 过期时间延长了
            CookieUtils.setCookie(request, response, jwtConfig.getCookieName(),
                    newToken, jwtConfig.getCookieMaxAge(), true);

            return ResponseEntity.ok(aa);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


    }

}
