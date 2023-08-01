package com.example.core.repository.schedule;

import com.example.core.model.schedule.VacationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationInfoRepository extends JpaRepository<VacationInfo, Long> {}
