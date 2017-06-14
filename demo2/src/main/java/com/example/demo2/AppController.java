package com.example.demo2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

  private static final Logger LOG = LoggerFactory.getLogger(Demo2Application.class);

  private final String serverName;

  public AppController(@Value("${server.name}") String serverName) {
    this.serverName = serverName;
  }

  @GetMapping("/")
  public String hello() {
    LOG.info("Server ({}) api:/ responded", serverName);
    return "Hello World";
  }

  @GetMapping("/name")
  public String name() {
    LOG.info("Server ({}) api:/name responded", serverName);
    return serverName;
  }

  @GetMapping("/completable")
  public String completable() {
    LOG.info("Server ({}) api:/completable responded", serverName);
    return "yo!";
  }
}
