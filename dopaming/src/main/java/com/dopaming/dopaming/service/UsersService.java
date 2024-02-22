package com.dopaming.dopaming.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.dopaming.dopaming.domain.Users;
import com.dopaming.dopaming.repository.UsersRepository;
import com.dopaming.dopaming.requestDto.LoginDto;
import com.dopaming.dopaming.requestDto.PasswordDTO;
import com.dopaming.dopaming.requestDto.RegisterDto;
import com.dopaming.dopaming.responseDto.UserInfoDto;
import com.dopaming.dopaming.security.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    public String join(RegisterDto dto) {
        Optional<Users> user = usersRepository.findUsersByUser_email(dto.getEmail());

        if(user.isPresent()) return "duplicate";

        usersRepository.save(new Users(
                        dto.getNickname(),
                        dto.getEmail(),
                        encoder.encode(dto.getPassword()))
        );

        return "success";
    }

    public String login(LoginDto dto) {
        Optional<Users> users = usersRepository.findUsersByUser_email(dto.getEmail());
        String token = null;

        if(users.isPresent() && encoder.matches(dto.getPassword(), users.get().getPassword())) {
            token = Util.createJwt(users.get().getId(), secretKey);
        }

        return token;
    }

    public boolean checkPassword(Long userId, String password) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        return encoder.matches(password, users.getPassword());
    }

    @Transactional
    public void editPassword(Long userId, PasswordDTO dto) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        users.changePassword(encoder.encode(dto.getPassword()));
    }

    public UserInfoDto userInformation(Long userId) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        return new UserInfoDto(users.getUser_email(), users.getUser_name());
    }

    @Transactional
    public String editUserInformation(Long userId, UserInfoDto dto) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        if(!users.getUser_email().equals(dto.getEmail())) {
            Optional<Users> usersByUserEmail = usersRepository.findUsersByUser_email(dto.getEmail());

            if(usersByUserEmail.isPresent()) return "email duplicate";

            users.setUser_email(dto.getEmail());
        }

        if(!users.getUser_name().equals(dto.getName())) {
            Optional<Users> usersByUserName = usersRepository.findUsersByUser_name(dto.getName());

            if(usersByUserName.isPresent()) return "name duplicate";

            users.setUser_name(dto.getName());
        }

        return "success";
    }
}
