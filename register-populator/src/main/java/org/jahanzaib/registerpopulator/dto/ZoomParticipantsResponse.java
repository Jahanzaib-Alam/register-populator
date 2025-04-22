package org.jahanzaib.registerpopulator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ZoomParticipantsResponse(
	List<ZoomParticipant> participants
) {}
