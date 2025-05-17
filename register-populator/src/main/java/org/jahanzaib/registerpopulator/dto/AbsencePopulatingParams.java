package org.jahanzaib.registerpopulator.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record AbsencePopulatingParams(
    LocalDate dateToUpdate,
    String registerSpreadsheetId,
    String absenceFormSpreadsheetId,
    String absenceFormSheetName,
    int absenceYearGroup) {}
