package org.jahanzaib.registerpopulator.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jahanzaib.registerpopulator.dto.AbsencePopulatingParams;
import org.jahanzaib.registerpopulator.dto.AttendanceSourceParams;
import org.jahanzaib.registerpopulator.util.DateUtil;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleSheetsService {
  private final Sheets sheetsService;

  private static final DateTimeFormatter SOURCE_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

  public List<String> getNamesForDateFromAttendanceForm(
      AttendanceSourceParams attendanceSourceParams) {
    List<String> matchedNames = new ArrayList<>();

    List<List<Object>> sourceSheetValues;
    try {
      ValueRange sourceSheetValueRange =
          sheetsService
              .spreadsheets()
              .values()
              .get(
                  attendanceSourceParams.attendanceFormSpreadsheetId(),
                  attendanceSourceParams.attendanceFormSheetName() + "!A:B")
              .execute();
      sourceSheetValues =
          sourceSheetValueRange != null
              ? sourceSheetValueRange.getValues()
              : Collections.emptyList();
    } catch (IOException e) {
      log.error("Error getting attendance form sheet {}", e.getMessage(), e);
      return null;
    }

    for (int i = 1; i < sourceSheetValues.size(); i++) { // start from 1 to skip header
      List<Object> row = sourceSheetValues.get(i);
      if (!row.isEmpty()) {
        String rowDateString = row.get(0).toString();
        if (LocalDate.parse(rowDateString, SOURCE_DATE_FORMATTER)
            .equals(attendanceSourceParams.dateToUpdate())) {
          matchedNames.add(row.get(1).toString()); // name column
        }
      }
    }
    return matchedNames;
  }

  public List<String> getNamesForDateFromAbsenceForm(AbsencePopulatingParams params) {
    List<List<Object>> sourceSheetValues;
    try {
      ValueRange sourceSheetValueRange =
          sheetsService
              .spreadsheets()
              .values()
              .get(params.absenceFormSpreadsheetId(), params.absenceFormSheetName() + "!A:I")
              .execute();
      sourceSheetValues =
          sourceSheetValueRange != null
              ? sourceSheetValueRange.getValues()
              : Collections.emptyList();
    } catch (IOException e) {
      log.error("Error getting attendance form sheet {}", e.getMessage(), e);
      return null;
    }
    return extractMatchedNamesFromSheetValues(
        params.dateToUpdate(), sourceSheetValues, params.absenceYearGroup());
  }

  private List<String> extractMatchedNamesFromSheetValues(
      LocalDate dateToUpdate, List<List<Object>> sourceSheetValues, int absenceYearGroup) {
    List<String> matchedNames = new ArrayList<>();
    for (int i = 1; i < sourceSheetValues.size(); i++) { // start from 1 to skip header
      List<Object> row = sourceSheetValues.get(i);
      if (!row.isEmpty()) {
        String rowDateString = row.get(4).toString();
        LocalDate rowDate = DateUtil.tryParseDateString(rowDateString);
        String rowPersonName = row.get(2).toString();
        if (rowDate == null) {
          log.debug(
              "Unable to parse date at row {}. Date given was {} for person named {}",
              i + 1,
              rowDateString,
              rowPersonName);
          continue;
        }
        boolean rowAttendingOnlineString = "Yes".equals(row.get(5).toString());
        boolean isForSelectedYearGroup = ("Year " + absenceYearGroup).equals(row.get(8).toString());
        if (rowDate.equals(dateToUpdate) && isForSelectedYearGroup && !rowAttendingOnlineString) {
          matchedNames.add(rowPersonName);
        }
      }
    }
    return matchedNames;
  }

  public void createSheetWithNames(
      String newSheetName, List<String> matchedNames, String registerSpreadsheetId) {
    boolean newSheetCreated = createNewSheetInRegisterForDate(newSheetName, registerSpreadsheetId);
    if (newSheetCreated) {
      addMatchedNamesToNewSheet(newSheetName, matchedNames, registerSpreadsheetId);
    }
  }

  public void addMatchedNamesToNewSheet(
      String newSheetName, List<String> matchedNames, String registerSpreadsheetId) {
    List<List<Object>> writeValues =
        matchedNames.stream().map(Collections::<Object>singletonList).collect(Collectors.toList());

    ValueRange body = new ValueRange().setValues(writeValues);

    try {
      sheetsService
          .spreadsheets()
          .values()
          .update(registerSpreadsheetId, newSheetName + "!A1", body)
          .setValueInputOption("RAW")
          .execute();
    } catch (IOException e) {
      log.error("Error updating new sheet in attendance register {}", e.getMessage(), e);
    }
  }

  public boolean createNewSheetInRegisterForDate(
      String newSheetName, String registerSpreadsheetId) {
    AddSheetRequest addSheetRequest =
        new AddSheetRequest().setProperties(new SheetProperties().setTitle(newSheetName));
    BatchUpdateSpreadsheetRequest request =
        new BatchUpdateSpreadsheetRequest()
            .setRequests(List.of(new Request().setAddSheet(addSheetRequest)));

    try {
      sheetsService.spreadsheets().batchUpdate(registerSpreadsheetId, request).execute();
    } catch (IOException e) {
      log.error("Error creating new sheet in attendance register {}", e.getMessage(), e);
      return false;
    }
    return true;
  }
}
