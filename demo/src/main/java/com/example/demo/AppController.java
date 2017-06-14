package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class AppController {

  private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

  private final RestTemplate restTemplate;
  private final String endpoint;
  private final String serverName;

  @Autowired
  public AppController(RestTemplate restTemplate, @Value("${endpoint}") String endpoint,
      @Value("${server.name}") String serverName) {
    this.restTemplate = restTemplate;
    this.endpoint = endpoint;
    this.serverName = serverName;
  }

  @GetMapping("/")
  @ResponseBody
  public String hello() {
    LOG.info("Server ({}) calling endpoint {}", endpoint);
    return restTemplate.getForEntity(endpoint, String.class).getBody();
  }
}
