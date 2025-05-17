package org.jahanzaib.registerpopulator.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record AttendanceSourceParams(
    LocalDate dateToUpdate,
    String registerSpreadsheetId,
    String attendanceFormSpreadsheetId,
    String attendanceFormSheetName) {}
