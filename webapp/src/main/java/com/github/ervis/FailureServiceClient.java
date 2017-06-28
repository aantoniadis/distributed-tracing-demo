package com.github.ervis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

class FailureServiceClient {

  private final String wiremockUrl;
  private final RestTemplate restTemplate;

  FailureServiceClient(String wiremockUrl, RestTemplate restTemplate) {
    this.wiremockUrl = wiremockUrl;
    this.restTemplate = restTemplate;
  }


  List<FailureResponse> getFailureBaggage(HttpServletRequest request,
      String controller) {
    Map<String, Object> map = toMap(request, controller);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    FailureResponse[] data = restTemplate
        .postForObject(failureBaggageUrl(), new HttpEntity<>(map, headers),
            FailureResponse[].class);

    return Arrays.asList(data);
  }

  private Map<String, Object> toMap(HttpServletRequest request,
      String controller) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("method", request.getMethod());
    map.put("contactPoint", controller);

    return map;
  }


  private String failureBaggageUrl() {
    return wiremockUrl + "/failures";
  }
}
