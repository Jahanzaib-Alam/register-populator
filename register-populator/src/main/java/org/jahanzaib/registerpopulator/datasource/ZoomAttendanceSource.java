package org.jahanzaib.registerpopulator.datasource;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class ZoomAttendanceSource implements AttendanceSource {
	public List<String> getNamesForDateFromAttendanceForm(LocalDate dateToUpdate) {
		return Collections.emptyList();
	}
}
