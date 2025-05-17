package org.jahanzaib.registerpopulator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ZoomMeetingListResponse(List<ZoomMeeting> meetings) {}
