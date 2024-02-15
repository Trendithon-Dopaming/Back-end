package com.dopaming.dopaming.controller;

import com.dopaming.dopaming.requestDto.LoginDto;
import com.dopaming.dopaming.requestDto.RegisterDto;
import com.dopaming.dopaming.security.Util;
import com.dopaming.dopaming.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequiredArgsConstructor
public class UsersController {
    @Value("${jwt.secret}")
    private String secretKey;

    private final Util util;

    private final UsersService usersService;

    @PostMapping("/user")
    public void register(@RequestBody RegisterDto dto) {
        usersService.join(dto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto dto) {
        String token = usersService.login(dto);

        if(token == null) {
            return "fail";
        }

        Cookie cookie  = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(86400);
        cookie.setHttpOnly(true);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addCookie(cookie);

        return "success";
    }

    // 토큰 아래처럼 사용하시면 됩니다
    @GetMapping("/test")
    public String test(@CookieValue(name = "token") String token){
        // 현재 로그인중인 Users 기본키 가져오기
        Long userId = util.getUserId(token, secretKey);
        System.out.println("userId: " + userId);
        return token;
    }
}
