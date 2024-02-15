package com.dopaming.dopaming.repository;

import com.dopaming.dopaming.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
