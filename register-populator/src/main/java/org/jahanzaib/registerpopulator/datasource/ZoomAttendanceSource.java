package org.jahanzaib.registerpopulator.datasource;

import lombok.RequiredArgsConstructor;
import org.jahanzaib.registerpopulator.service.ZoomService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ZoomAttendanceSource implements AttendanceSource {
	private final ZoomService zoomService;

	@Override
	public List<String> getNamesForDateFromAttendanceForm(LocalDate dateToUpdate) {
		List<String> attendees = new ArrayList<>();
		attendees.add("--ZOOM PARTICIPANTS BELOW--");
		attendees.addAll(zoomService.getNamesForDateFromAttendanceForm(dateToUpdate));

		return attendees;
	}
}
