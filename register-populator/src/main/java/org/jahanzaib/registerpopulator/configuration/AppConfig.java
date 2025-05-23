package org.jahanzaib.registerpopulator.configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  private static final String APPLICATION_NAME = "Register Populator";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final List<String> SCOPES = List.of(SheetsScopes.SPREADSHEETS);

  @Value("${google.credentials-json}")
  private String googleCredentialsJson;

  @Bean
  public Sheets sheetsService() throws IOException, GeneralSecurityException {
    NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    // Load service account credentials
    GoogleCredentials credentials =
        GoogleCredentials.fromStream(
                new ByteArrayInputStream(googleCredentialsJson.getBytes(StandardCharsets.UTF_8)))
            .createScoped(SCOPES);

    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

    return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
