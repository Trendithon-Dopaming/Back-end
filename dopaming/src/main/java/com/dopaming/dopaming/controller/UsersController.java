package com.dopaming.dopaming.controller;

import com.dopaming.dopaming.requestDto.RegisterDto;
import com.dopaming.dopaming.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/user")
    public void register(@RequestBody RegisterDto dto) {
        usersService.join(dto);
    }
}
