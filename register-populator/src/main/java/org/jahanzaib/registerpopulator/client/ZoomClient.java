package org.jahanzaib.registerpopulator.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jahanzaib.registerpopulator.dto.ZoomMeeting;
import org.jahanzaib.registerpopulator.dto.ZoomMeetingListResponse;
import org.jahanzaib.registerpopulator.dto.ZoomParticipant;
import org.jahanzaib.registerpopulator.dto.ZoomParticipantsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class ZoomClient {
  private final RestTemplate restTemplate;
  private final ZoomOAuthManager oAuthManager;

  @Value("${zoom.api-url}")
  private String apiUrl;

  @Value("${zoom.user-id}")
  private String userId;

  public Optional<ZoomMeeting> findMeetingByDate(LocalDate date) {
    String url = apiUrl + "/report/users/" + userId + "/meetings" + "?from=" + date + "&to=" + date;
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(oAuthManager.getAccessToken());
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<ZoomMeetingListResponse> response =
        restTemplate.exchange(url, HttpMethod.GET, entity, ZoomMeetingListResponse.class);

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      return response.getBody().meetings().stream()
          .filter(meeting -> meeting.start_time().startsWith(date.toString()))
          .findFirst();
    }

    return Optional.empty();
  }

  public List<String> getAttendeeNames(String meetingUuid) {
    String encodedUuid = URLEncoder.encode(meetingUuid, StandardCharsets.UTF_8);
    String url = apiUrl + "/past_meetings/" + encodedUuid + "/participants?page_size=50";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(oAuthManager.getAccessToken());
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<ZoomParticipantsResponse> response =
        restTemplate.exchange(url, HttpMethod.GET, entity, ZoomParticipantsResponse.class);

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      return response.getBody().participants().stream().map(ZoomParticipant::name).toList();
    }
    return Collections.emptyList();
  }
}
