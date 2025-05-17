package org.jahanzaib.registerpopulator.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateUtil {
  private static final List<DateTimeFormatter> DATE_FORMATTERS =
      List.of(
          // Slash-separated
          DateTimeFormatter.ofPattern("d/M/yy"),
          DateTimeFormatter.ofPattern("d/M/yyyy"),
          // Dot-separated
          DateTimeFormatter.ofPattern("d.M.yy"),
          DateTimeFormatter.ofPattern("d.M.yyyy"),
          // Dash-separated
          DateTimeFormatter.ofPattern("d-M-yy"),
          DateTimeFormatter.ofPattern("d-M-yyyy"));

  /**
   * Uses multiple date patterns to try to convert a date string
   *
   * @param dateString String to attempt to parse into a date
   * @return parsed date
   */
  public static LocalDate tryParseDateString(String dateString) {
    for (DateTimeFormatter formatter : DATE_FORMATTERS) {
      try {
        return LocalDate.parse(dateString, formatter);
      } catch (DateTimeParseException ignored) {
        // Try next formatter
      }
    }
    return null;
  }
}
