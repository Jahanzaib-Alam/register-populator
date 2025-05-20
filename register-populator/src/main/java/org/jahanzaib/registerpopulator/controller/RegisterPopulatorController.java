package org.jahanzaib.registerpopulator.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jahanzaib.registerpopulator.dto.PopulateRequestDTO;
import org.jahanzaib.registerpopulator.processor.AttendanceProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register-populator")
@RequiredArgsConstructor
@Slf4j
public class RegisterPopulatorController {

  private final AttendanceProcessor attendanceProcessor;

  @Value("${friendCode}")
  private String friendCode;

  @RateLimiter(name = "registerPopulatorApiLimiter")
  @PostMapping("/populate")
  public ResponseEntity<?> populateAttendancesAndAbsences(
      @Valid @RequestBody PopulateRequestDTO populateRequest) {
    if (!Objects.equals(friendCode, populateRequest.getFriendCode())) {
      return ResponseEntity.badRequest().build();
    }
    log.info("Running application for date: {}", populateRequest.getDateToUpdate());
    attendanceProcessor.processAttendanceAndAbsences(populateRequest);
    return ResponseEntity.ok().build();
  }
}
