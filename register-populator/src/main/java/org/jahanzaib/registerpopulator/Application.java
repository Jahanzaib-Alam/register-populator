package org.jahanzaib.registerpopulator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jahanzaib.registerpopulator.processor.AttendanceProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class Application implements CommandLineRunner {
	private final AttendanceProcessor attendanceProcessor;

	private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Application start...");

		LocalDate dateToUpdate;
		try {
			dateToUpdate = LocalDate.parse(args[0], INPUT_DATE_FORMATTER);
		} catch (Exception e) {
			log.error("Invalid command line date argument passed. Expected format is dd/MM/yyyy. Program will stop.");
			return;
		}
		log.info("Running application for date: {}", dateToUpdate);

		attendanceProcessor.processAttendance(dateToUpdate);
        attendanceProcessor.processAbsences(dateToUpdate);

		System.out.println("Application end...");
	}
}