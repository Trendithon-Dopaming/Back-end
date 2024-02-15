package com.dopaming.dopaming.service;

import com.dopaming.dopaming.domain.Users;
import com.dopaming.dopaming.repository.UsersRepository;
import com.dopaming.dopaming.requestDto.LoginDto;
import com.dopaming.dopaming.requestDto.RegisterDto;
import com.dopaming.dopaming.security.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder encoder;


    @Value("${jwt.secret}")
    private String secretKey;

    @Transactional
    public void join(RegisterDto dto) {
        usersRepository.save(new Users(
                        dto.getNickname(),
                        dto.getEmail(),
                        encoder.encode(dto.getPassword()))
        );
    }

    public String login(LoginDto dto) {
        Optional<Users> users = usersRepository.findUsersByUser_email(dto.getEmail());
        String token = null;

        if(users.isPresent() && encoder.matches(dto.getPassword(), users.get().getPassword())) {
            token = Util.createJwt(users.get().getId(), secretKey);
        }

        return token;
    }
}
