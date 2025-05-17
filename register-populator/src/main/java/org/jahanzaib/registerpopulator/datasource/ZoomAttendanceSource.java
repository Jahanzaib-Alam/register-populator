package org.jahanzaib.registerpopulator.datasource;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jahanzaib.registerpopulator.dto.AttendanceSourceParams;
import org.jahanzaib.registerpopulator.service.ZoomService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ZoomAttendanceSource implements AttendanceSource {
  private final ZoomService zoomService;

  @Override
  public List<String> getNamesForDateFromAttendanceForm(AttendanceSourceParams params) {
    List<String> attendees = new ArrayList<>();
    attendees.add("--ZOOM PARTICIPANTS BELOW--");
    attendees.addAll(zoomService.getNamesForDateFromAttendanceForm(params.dateToUpdate()));

    return attendees;
  }
}
