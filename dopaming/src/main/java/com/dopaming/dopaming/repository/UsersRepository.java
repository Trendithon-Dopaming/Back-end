package com.dopaming.dopaming.repository;

import com.dopaming.dopaming.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("select u from Users u " +
            "where u.user_email = :user_email")
    Optional<Users> findUsersByUser_email(@Param("user_email") String user_email);
}
