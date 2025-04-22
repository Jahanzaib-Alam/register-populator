package org.jahanzaib.registerpopulator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ZoomMeeting(
		String uuid,
		String topic,
		String start_time
) {}