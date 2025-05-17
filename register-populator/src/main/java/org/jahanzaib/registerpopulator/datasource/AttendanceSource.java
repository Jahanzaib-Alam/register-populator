package org.jahanzaib.registerpopulator.datasource;

import java.util.List;
import org.jahanzaib.registerpopulator.dto.AttendanceSourceParams;

public interface AttendanceSource {
  List<String> getNamesForDateFromAttendanceForm(AttendanceSourceParams params);
}
