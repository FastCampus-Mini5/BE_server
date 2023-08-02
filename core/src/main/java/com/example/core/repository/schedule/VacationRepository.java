package com.example.core.repository.schedule;

import com.example.core.model.schedule.Status;
import com.example.core.model.schedule.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

    @Query("SELECT v FROM Vacation v WHERE YEAR(v.startDate) = :year")
    Page<Vacation> findByStartDateYear(@Param("year") int year, Pageable pageable);
    Page<Vacation> findVacationsByStatus(Pageable pageable, Status status);
}