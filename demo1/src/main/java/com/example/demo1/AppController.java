package com.example.demo1;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppController {

  private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

  private final RestTemplate restTemplate;
  private final String endpoint;
  private final String threadEnpoint;
  private final String completableEndpoint;
  private final String randomUrl;
  private final String serverName;

  @Autowired
  public AppController(RestTemplate restTemplate,
      @Value("${endpoint}") String endpoint,
      @Value("${thread.endpoint}") String threadEnpoint,
      @Value("${completable.endpoint}") String completableEndpoint,
      @Value("${random.endpoint}") String randomUrl,
      @Value("${server.name}") String serverName) {
    this.restTemplate = restTemplate;
    this.endpoint = endpoint;
    this.threadEnpoint = threadEnpoint;
    this.completableEndpoint = completableEndpoint;
    this.randomUrl = randomUrl;
    this.serverName = serverName;
  }

  @GetMapping("/")
  public String hello() {
    LOG.info("Server ({}) calling endpoint {}", serverName, endpoint);
    startThreadRequest();
    completableFutureCall(completableEndpoint);
    // Should throw exception :)
    completableFutureCall("http://localhost:8082/dev-random");

    return restTemplate.getForEntity(endpoint, String.class).getBody();
  }

  private void completableFutureCall(final String url) {
    LOG.info("Server ({}) calling completable future endpoint {}", serverName, url);
    CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(url, String.class).getBody())
        .thenAccept(
            res -> LOG
                .info("Server ({}) received response {} from endpoint: {}", serverName, res, url))
        .exceptionally(ex -> {
          LOG.error("Completable future failed with error: {}", ex.getMessage());
          return null;
        });
  }

  // start new Thread
  private void startThreadRequest() {
    new Thread(() -> {
      LOG.info("Server ({}) thread calling endpoint {}", serverName, threadEnpoint);
      restTemplate.getForEntity(threadEnpoint, String.class);
    }).run();
  }
}
