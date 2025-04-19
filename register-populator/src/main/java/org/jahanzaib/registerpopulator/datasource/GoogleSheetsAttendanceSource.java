package org.jahanzaib.registerpopulator.datasource;

import lombok.RequiredArgsConstructor;
import org.jahanzaib.registerpopulator.service.GoogleSheetsService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleSheetsAttendanceSource implements AttendanceSource {
	private final GoogleSheetsService sheetsService;

	@Override
	public List<String> getNamesForDateFromAttendanceForm(LocalDate dateToUpdate) {
		return sheetsService.getNamesForDateFromAttendanceForm(dateToUpdate);
	}
}
