package org.jahanzaib.registerpopulator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PopulateRequestDTO {

  @NotBlank private String registerSpreadsheetId;

  @NotNull(message = "dateToUpdate is required")
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate dateToUpdate;

  @NotBlank private String attendanceFormSpreadsheetId;
  @NotBlank private String attendanceFormSheetName;
  @NotBlank private String absenceFormSpreadsheetId;
  @NotBlank private String absenceFormSheetName;

  @JsonProperty("absence-year-group")
  @Min(value = 1, message = "absence-year-group must be at least 1")
  private int absenceYearGroup;

  @NotBlank private boolean checkZoom;
}
