package com.example.application.schedule.duty.service;

import com.example.application.schedule.duty.dto.DutyRequest;
import com.example.application.schedule.duty.dto.DutyResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception400;
import com.example.core.errors.exception.Exception403;
import com.example.core.errors.exception.Exception404;
import com.example.core.model.schedule.Duty;
import com.example.core.model.schedule.Status;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.DutyRepository;
import com.example.core.repository.user.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DutyService {

    private final DutyRepository dutyRepository;
    private final UserRepository userRepository;
    private final Encryption encryption;

    @Transactional
    public DutyResponse.DutyDTO requestDuty(DutyRequest.AddDTO dutyRequest, Long userId) {

        if (dutyRequest == null) throw new Exception400(ErrorMessage.EMPTY_DATA_TO_REQUEST_DUTY);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorMessage.USER_NOT_FOUND));

        Duty duty = dutyRequest.toEntityWith(user);

        Optional<Duty> existingDuty = dutyRepository.findByUserAndDutyDate(duty.getUser(), duty.getDutyDate());
        existingDuty.ifPresent(existing -> {
            throw new Exception400(ErrorMessage.DUTY_ALREADY_EXISTS);
        });

        Duty savedDuty = dutyRepository.save(duty);
        return DutyResponse.DutyDTO.from(savedDuty);
    }

    @Transactional
    public DutyResponse.DutyDTO cancelDuty(Long id, Long userId) {

        if (id == null) throw new Exception400(ErrorMessage.EMPTY_DATA_TO_CANCEL_DUTY);

        Duty duty = dutyRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorMessage.NOT_FOUND_DUTY));

        if (!duty.getUser().getId().equals(userId)) {
            throw new Exception403(ErrorMessage.UNAUTHORIZED_ACCESS_TO_DUTY);
        }

        if (duty.getStatus() == Status.APPROVE) {
            throw new Exception403(ErrorMessage.DUTY_CANNOT_BE_CANCELLED);
        }

        DutyResponse.DutyDTO cancelledDuty = DutyResponse.DutyDTO.from(duty);
        dutyRepository.deleteById(id);
        return cancelledDuty;
    }

    @Transactional(readOnly = true)
    public List<DutyResponse.MyDutyDTO> getMyDutiesByYear(int year, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorMessage.USER_NOT_FOUND));

        List<Duty> myDutiesInYear = dutyRepository.findByYearAndUser(year, user);
        return myDutiesInYear.stream().map(DutyResponse.MyDutyDTO::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DutyResponse.ListDTO> getAllDutiesByYear(int year) {
        List<Duty> allDutiesInYear = dutyRepository.findByDutyDateYear(year);
        return allDutiesInYear.stream()
                .map(DutyResponse.ListDTO::from)
                .map(dto->dto.decrypt(encryption))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public byte[] getAllDutiesExcelByYear(int year) throws IOException {
        List<DutyResponse.ListDTO> allDutiesInYear = getAllDutiesByYear(year);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Duties");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("유저네임");
        headerRow.createCell(1).setCellValue("상태");
        headerRow.createCell(2).setCellValue("당직일");

        for (int i = 0; i < allDutiesInYear.size(); i++) {
            DutyResponse.ListDTO duty = allDutiesInYear.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(duty.getUsername());
            row.createCell(1).setCellValue("당직");
            row.createCell(2).setCellValue(duty.getDutyDate().toString());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] bytes = outputStream.toByteArray();
        return bytes;
    }
}