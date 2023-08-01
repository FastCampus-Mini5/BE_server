package com.example.core.repository.schedule;

import com.example.core.model.schedule.Duty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DutyRepository extends JpaRepository<Duty, Long> {}
