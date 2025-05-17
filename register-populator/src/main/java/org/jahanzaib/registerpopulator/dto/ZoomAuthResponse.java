package org.jahanzaib.registerpopulator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ZoomAuthResponse(String access_token) {}
