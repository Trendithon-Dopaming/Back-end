package com.dopaming.dopaming.repository;

import com.dopaming.dopaming.domain.PamingSaves;
import com.dopaming.dopaming.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PamingSavesRepsitory extends JpaRepository<PamingSaves, Long> {

    Page<PamingSaves> findAllByUsers(Users user, Pageable pageable);
}
