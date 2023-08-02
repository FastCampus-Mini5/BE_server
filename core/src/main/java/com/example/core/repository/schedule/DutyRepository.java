package com.example.core.repository.schedule;

import com.example.core.model.schedule.Duty;
import com.example.core.model.schedule.Status;
import com.example.core.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;

public interface DutyRepository extends JpaRepository<Duty, Long> {

    Optional<Duty> findByUserAndDutyDate(User user, Timestamp dutyDate);

    Page<Duty> findDutyByStatus(Pageable pageable, Status status);

    @Query("SELECT d FROM Duty d WHERE YEAR(d.dutyDate) = :year")
    Page<Duty> findByDutyDateYear(@Param("year") int year, Pageable pageable);
}