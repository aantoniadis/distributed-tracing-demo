package com.github.ervis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppController {

  private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

  private final String url;
  private final RestTemplate restTemplate;

  @Autowired
  public AppController(@Value("${service1.url}") String url, RestTemplate restTemplate) {
    this.url = url;
    this.restTemplate = restTemplate;
  }

  @GetMapping("/")
  @ResponseBody
  public String hello() {

    String body = restTemplate.getForEntity(url, String.class).getBody();
    LOG.info("Response: {}", body);
    return body;
  }

  @GetMapping("/error")
  @ResponseBody
  public String error() {
    return "error";
  }
}
