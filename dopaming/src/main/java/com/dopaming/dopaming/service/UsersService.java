package com.dopaming.dopaming.service;

import com.dopaming.dopaming.domain.Users;
import com.dopaming.dopaming.repository.UsersRepository;
import com.dopaming.dopaming.requestDto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public void join(RegisterDto dto) {
        usersRepository.save(new Users(dto.getNickname(), dto.getEmail(), dto.getPassword()));
    }
}
