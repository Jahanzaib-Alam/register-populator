package org.jahanzaib.registerpopulator.datasource;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceSource {
	List<String> getNamesForDateFromAttendanceForm(LocalDate dateToUpdate);
}
