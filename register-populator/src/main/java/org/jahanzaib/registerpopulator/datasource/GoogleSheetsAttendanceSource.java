package org.jahanzaib.registerpopulator.datasource;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jahanzaib.registerpopulator.dto.AttendanceSourceParams;
import org.jahanzaib.registerpopulator.service.GoogleSheetsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleSheetsAttendanceSource implements AttendanceSource {
  private final GoogleSheetsService sheetsService;

  @Override
  public List<String> getNamesForDateFromAttendanceForm(AttendanceSourceParams params) {
    return sheetsService.getNamesForDateFromAttendanceForm(params);
  }
}
