package com.example.application.schedule.vacationInfo.service;

import com.example.core.model.schedule.VacationInfo;
import com.example.core.repository.schedule.VacationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AutoVacationPaymentService {

    private final VacationInfoRepository vacationInfoRepository;

    @Transactional
    @Scheduled(fixedRate = 1000 * 60)
    public void paymentVacation() {
        log.info("연차 자동 지급 스케줄러 동작");
        List<VacationInfo> allUserAndVacationInfo = vacationInfoRepository.findAll();
        allUserAndVacationInfo.forEach(VacationInfo::increaseVacation);
    }
}
