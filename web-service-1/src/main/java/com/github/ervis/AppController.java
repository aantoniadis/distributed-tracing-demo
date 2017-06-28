package com.github.ervis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppController {

  private static final Logger LOG = LoggerFactory.getLogger(WSApplication1.class);

  private final Executor executor;
  private final RestTemplate restTemplate;
  private final String endpoint;
  private final String serverName;

  @Autowired
  public AppController(Executor executor,
      RestTemplate restTemplate,
      @Value("${endpoint}") String endpoint,
      @Value("${server.name}") String serverName) {

    this.executor = executor;
    this.restTemplate = restTemplate;
    this.endpoint = endpoint;
    this.serverName = serverName;
  }

  @GetMapping("/")
  public String hello() {
    LOG.info("Spring controller hello() is running");

    startThreadRequest();

    completableFutureCallRunAsync(endpoint);

    completableFutureCallSupplyAsync(endpoint);

    return blockingCall();
  }

  // start new Thread
  private void startThreadRequest() {
    new Thread(() -> {
      LOG.info("startThreadRequest: Calling endpoint {}", endpoint);
      String body = restTemplate.getForEntity(endpoint, String.class).getBody();
      LOG.info("startThreadRequest: HTTP response( {} )", body);
    }).run();
  }

  private void completableFutureCallRunAsync(final String url) {
    CompletableFuture
        .runAsync(() -> {
          LOG.info("CompletableFuture.runAsync: Calling endpoint {}", url);
          String response = restTemplate.getForEntity(url, String.class).getBody();
          LOG.info("CompletableFuture.runAsync: HTTP response( {} )", response);
        }, executor);
  }

  private void completableFutureCallSupplyAsync(final String url) {
    CompletableFuture
        .supplyAsync(() -> {
          LOG.info("CompletableFuture.supplyAsync: Calling endpoint {}", url);
          return restTemplate.getForEntity(url, String.class).getBody();
        }, executor)
        .thenAccept(res -> LOG.info("CompletableFuture.thenAccept: HTTP response( {} )", res))
        .exceptionally(ex -> {
          LOG.error("CompletableFuture.exceptionally: {}", ex.getMessage());
          return null;
        });
  }

  private String blockingCall() {
    LOG.info("AppController.hello(): RestTemplate call to {}", endpoint);
    String response = restTemplate.getForEntity(endpoint, String.class).getBody();
    LOG.info("AppController.hello(): HTTP response( {} )", response);

    return response;
  }
}
