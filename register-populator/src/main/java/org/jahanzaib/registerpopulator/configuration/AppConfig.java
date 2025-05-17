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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  private static final String APPLICATION_NAME = "Register Populator";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final List<String> SCOPES = List.of(SheetsScopes.SPREADSHEETS);
  private static final String CREDENTIALS_FILE_PATH = "/service-account.json";

  @Bean
  public Sheets sheetsService() throws IOException, GeneralSecurityException {
    NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    InputStream credentialsFileStream = AppConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (credentialsFileStream == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    // Load service account credentials from JSON
    GoogleCredentials credentials =
        GoogleCredentials.fromStream(credentialsFileStream).createScoped(SCOPES);

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
