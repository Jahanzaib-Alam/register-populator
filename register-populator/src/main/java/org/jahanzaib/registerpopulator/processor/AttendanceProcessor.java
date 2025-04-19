package org.jahanzaib.registerpopulator.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jahanzaib.registerpopulator.datasource.AttendanceSource;
import org.jahanzaib.registerpopulator.service.GoogleSheetsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceProcessor {
	private final GoogleSheetsService sheetsService;
	private final List<AttendanceSource> attendanceSources;

	public void processAttendance(LocalDate dateToUpdate) {
		List<String> namesOfAttendeesForDate = new ArrayList<>();

		for (AttendanceSource attendanceSource : attendanceSources) {
			namesOfAttendeesForDate = attendanceSource.getNamesForDateFromAttendanceForm(dateToUpdate);
		}

		if (CollectionUtils.isEmpty(namesOfAttendeesForDate)) {
			log.info("No names were found to add to register, exiting program...");
			return;
		}
		sheetsService.createSheetWithMatchedNamesForDate(dateToUpdate, namesOfAttendeesForDate);
	}

	public void processAbsences(LocalDate dateToUpdate) {
		List<String> namesOfAttendeesForDate = sheetsService.getNamesForDateFromAbsenceForm(dateToUpdate);

	}
}
