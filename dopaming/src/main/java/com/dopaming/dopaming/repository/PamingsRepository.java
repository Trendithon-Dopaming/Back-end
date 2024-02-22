package com.dopaming.dopaming.repository;

import com.dopaming.dopaming.domain.Pamings;
import com.dopaming.dopaming.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PamingsRepository extends JpaRepository<Pamings, Long> {
    List<Pamings> findAllByUsers(Users user);
}
