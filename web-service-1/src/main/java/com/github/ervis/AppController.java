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
  private final String threadEnpoint;
  private final String completableEndpoint;
  private final String serverName;

  @Autowired
  public AppController(Executor executor,
      RestTemplate restTemplate,
      @Value("${endpoint}") String endpoint,
      @Value("${thread.endpoint}") String threadEnpoint,
      @Value("${completable.endpoint}") String completableEndpoint,
      @Value("${server.name}") String serverName) {

    this.executor = executor;
    this.restTemplate = restTemplate;
    this.endpoint = endpoint;
    this.threadEnpoint = threadEnpoint;
    this.completableEndpoint = completableEndpoint;
    this.serverName = serverName;
  }

  @GetMapping("/")
  public String hello() {
    LOG.info("Spring controller hello() is running");
    startThreadRequest();
    completableFutureCallRunAsync(completableEndpoint);
    completableFutureCallSupplyAsync(completableEndpoint);

    LOG.info("AppController.hello(): RestTemplate call to {}", endpoint);
    return restTemplate.getForEntity(endpoint, String.class).getBody();
  }

  private void completableFutureCallRunAsync(final String url) {
    LOG.info("Server ({}) calling completable(runAsync) future endpoint {}", serverName, url);

    CompletableFuture
        .runAsync(() -> {
          LOG.info("CompletableFuture.runAsync: Calling endpoint {}", url);
          restTemplate.getForEntity(url, String.class).getBody();
        }, executor);
  }

  private void completableFutureCallSupplyAsync(final String url) {
    LOG.info("Server ({}) calling completable(supplyAsync) future endpoint {}", serverName, url);

    CompletableFuture
        .supplyAsync(() -> {
          LOG.info("CompletableFuture.supplyAsync: Calling endpoint {}", url);
          return restTemplate.getForEntity(url, String.class).getBody();
        }, executor)
        .thenAccept(res -> LOG.info("CompletableFuture.thenAccept: {}", res))
        .exceptionally(ex -> {
          LOG.error("CompletableFuture.exceptionally: {}", ex.getMessage());
          return null;
        });
  }

  // start new Thread
  private void startThreadRequest() {
    new Thread(() -> {
      LOG.info("startThreadRequest: Calling endpoint {}", threadEnpoint);
      String body = restTemplate.getForEntity(threadEnpoint, String.class).getBody();
      LOG.info("startThreadRequest: Received response '{}'", body);
    }).run();
  }
}
