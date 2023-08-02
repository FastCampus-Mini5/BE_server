package com.example.core.repository.schedule;

import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacationInfoRepository extends JpaRepository<VacationInfo, Long> {
    Optional<VacationInfo> findByUser(User user);
}