package org.jahanzaib.registerpopulator.processor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jahanzaib.registerpopulator.datasource.AttendanceSource;
import org.jahanzaib.registerpopulator.dto.AbsencePopulatingParams;
import org.jahanzaib.registerpopulator.dto.AttendanceSourceParams;
import org.jahanzaib.registerpopulator.dto.PopulateRequestDTO;
import org.jahanzaib.registerpopulator.service.GoogleSheetsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceProcessor {
  private final GoogleSheetsService sheetsService;
  private final List<AttendanceSource> attendanceSources;

  private static final DateTimeFormatter NEW_SHEET_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyMMdd");

  public void processAttendanceAndAbsences(PopulateRequestDTO populateRequestDTO) {
    processAttendance(populateRequestDTO);
    processAbsences(populateRequestDTO);
  }

  public void processAttendance(PopulateRequestDTO populateRequest) {
    List<String> namesOfAttendeesForDate = new ArrayList<>();

    AttendanceSourceParams attendanceSourceParams =
        AttendanceSourceParams.builder()
            .dateToUpdate(populateRequest.getDateToUpdate())
            .registerSpreadsheetId(populateRequest.getRegisterSpreadsheetId())
            .attendanceFormSpreadsheetId(populateRequest.getAttendanceFormSpreadsheetId())
            .attendanceFormSheetName(populateRequest.getAttendanceFormSheetName())
            .checkZoom(populateRequest.isCheckZoom())
            .build();

    for (AttendanceSource attendanceSource : attendanceSources) {
      namesOfAttendeesForDate.addAll(
          attendanceSource.getNamesForDateFromAttendanceForm(attendanceSourceParams));
    }

    if (CollectionUtils.isEmpty(namesOfAttendeesForDate)) {
      log.info("No names were found to add to register, exiting attendance method...");
      return;
    }
    String newSheetName = populateRequest.getDateToUpdate().format(NEW_SHEET_DATE_FORMATTER);
    sheetsService.createSheetWithNames(
        newSheetName, namesOfAttendeesForDate, populateRequest.getRegisterSpreadsheetId());
  }

  public void processAbsences(PopulateRequestDTO populateRequest) {
    var params =
        AbsencePopulatingParams.builder()
            .absenceFormSpreadsheetId(populateRequest.getAbsenceFormSpreadsheetId())
            .absenceFormSheetName(populateRequest.getAttendanceFormSheetName())
            .absenceYearGroup(populateRequest.getAbsenceYearGroup())
            .absenceYearGroup(populateRequest.getAbsenceYearGroup())
            .build();
    List<String> namesOfAbsenteesForDate = sheetsService.getNamesForDateFromAbsenceForm(params);
    log.info("Names of absentees: {}", namesOfAbsenteesForDate);
    String newSheetName = "A-" + populateRequest.getDateToUpdate().format(NEW_SHEET_DATE_FORMATTER);
    sheetsService.createSheetWithNames(
        newSheetName, namesOfAbsenteesForDate, populateRequest.getRegisterSpreadsheetId());
  }
}
