package org.jahanzaib.registerpopulator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ZoomParticipant(String name, String user_email) {}
