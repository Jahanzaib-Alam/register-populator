package org.jahanzaib.registerpopulator.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jahanzaib.registerpopulator.client.ZoomClient;
import org.jahanzaib.registerpopulator.dto.ZoomMeeting;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoomService {
  private final ZoomClient zoomClient;

  public List<String> getNamesForDateFromAttendanceForm(LocalDate dateToUpdate) {
    Optional<ZoomMeeting> meetingForDate = zoomClient.findMeetingByDate(dateToUpdate);
    if (meetingForDate.isEmpty()) {
      log.info("No Zoom meeting found for date {}, no participants will be fetched.", dateToUpdate);
      return Collections.emptyList();
    }
    return zoomClient.getAttendeeNames(meetingForDate.get().uuid());
  }
}
