package com.dopaming.dopaming.repository;

import com.dopaming.dopaming.domain.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StepsRepository extends JpaRepository<Steps, Long> {
    @Query("select s from Steps s " +
            "where s.pamings.id = :pamingsId and " +
            "s.step = :step")
    List<Steps> findAllByPamingsIdAndStep(@Param("pamingsId") Long pamingsId,
                                          @Param("step") int step);
}
