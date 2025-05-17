package org.jahanzaib.registerpopulator.client;

import lombok.RequiredArgsConstructor;
import org.jahanzaib.registerpopulator.dto.ZoomAuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class ZoomOAuthManager {
  private final RestTemplate restTemplate;

  @Value("${zoom.client-id}")
  private String clientId;

  @Value("${zoom.client-secret}")
  private String clientSecret;

  @Value("${zoom.account-id}")
  private String accountId;

  public String getAccessToken() {
    String url =
        UriComponentsBuilder.fromUriString("https://zoom.us/oauth/token")
            .queryParam("grant_type", "account_credentials")
            .queryParam("account_id", accountId)
            .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(clientId, clientSecret);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<?> request = new HttpEntity<>(headers);

    var response = restTemplate.exchange(url, HttpMethod.POST, request, ZoomAuthResponse.class);
    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      return response.getBody().access_token();
    }
    return null;
  }
}
