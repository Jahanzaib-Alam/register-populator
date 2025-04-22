package org.jahanzaib.registerpopulator.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jahanzaib.registerpopulator.datasource.AttendanceSource;
import org.jahanzaib.registerpopulator.service.GoogleSheetsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceProcessor {
	private final GoogleSheetsService sheetsService;
	private final List<AttendanceSource> attendanceSources;

	private static final DateTimeFormatter NEW_SHEET_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

	public void processAttendance(LocalDate dateToUpdate) {
		List<String> namesOfAttendeesForDate = new ArrayList<>();

		for (AttendanceSource attendanceSource : attendanceSources) {
			namesOfAttendeesForDate.addAll(attendanceSource.getNamesForDateFromAttendanceForm(dateToUpdate));
		}

		if (CollectionUtils.isEmpty(namesOfAttendeesForDate)) {
			log.info("No names were found to add to register, exiting attendance method...");
			return;
		}
		String newSheetName = dateToUpdate.format(NEW_SHEET_DATE_FORMATTER);
		sheetsService.createSheetWithNames(newSheetName, namesOfAttendeesForDate);
	}

	public void processAbsences(LocalDate dateToUpdate) {
		List<String> namesOfAbsenteesForDate = sheetsService.getNamesForDateFromAbsenceForm(dateToUpdate);
		if (CollectionUtils.isEmpty(namesOfAbsenteesForDate)) {
			log.info("No names were found to record absences for, exiting absences method...");
			return;
		}
		log.info("Names of absentees: {}", namesOfAbsenteesForDate);
		String newSheetName = "A-" + dateToUpdate.format(NEW_SHEET_DATE_FORMATTER);
		sheetsService.createSheetWithNames(newSheetName, namesOfAbsenteesForDate);
	}
}
