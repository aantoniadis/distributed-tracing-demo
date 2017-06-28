package com.github.ervis;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

  private static final Logger LOG = LoggerFactory.getLogger(WSApplication2.class);

  private final String serverName;

  public AppController(@Value("${server.name}") String serverName) {
    this.serverName = serverName;
  }

  @GetMapping("/")
  public String hello() {
    String random = String.valueOf(new Random().nextInt(1000000));
    LOG.info("Generated response: {}", random);
    return random;
  }
}
