package com.github.ervis;

import brave.Tracing;
import brave.context.slf4j.MDCCurrentTraceContext;
import brave.http.HttpTracing;
import brave.propagation.CurrentTraceContext;
import brave.spring.web.TracingClientHttpRequestInterceptor;
import brave.spring.webmvc.TracingHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.Sender;
import zipkin.reporter.urlconnection.URLConnectionSender;

@Configuration
@EnableWebMvc
public class AppConfig {

  @Bean
  public ClientHttpRequestFactory factory() {
    return new OkHttp3ClientHttpRequestFactory();
  }

  @Bean
  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
    return new RestTemplate(factory);
  }

  // Sends data to zipkin
  @Bean
  Sender sender(@Value("${zipkin.endpoint}") String zipkinEndpoint) {
    return URLConnectionSender.create(zipkinEndpoint);
  }

  @Bean
  CurrentTraceContext traceContext() {
    return MDCCurrentTraceContext.create();
  }

  @Bean(destroyMethod = "close")
  AsyncReporter reporter(Sender sender) throws Exception {
    return AsyncReporter.create(sender);
  }

  @Bean
  Tracing tracing(CurrentTraceContext traceContext, Reporter reporter) {
    return Tracing.newBuilder()
        .localServiceName("web-mvc-app")
        .reporter(reporter)
        .currentTraceContext(traceContext)
        .build();
  }

  @Bean
  HttpTracing httpTracing(Tracing tracing) {
    return HttpTracing.create(tracing);
  }

  // MVC interceptor
  @Bean
  AsyncHandlerInterceptor mvcInterceptor(HttpTracing tracing) {
    return TracingHandlerInterceptor.create(tracing);
  }

  // RestTemplate Interceptor
  @Bean
  ClientHttpRequestInterceptor clientHttpRequestInterceptor(Tracing tracing) {
    return TracingClientHttpRequestInterceptor.create(tracing);
  }

  // Register interceptor to all RestTemplates
  @Bean
  FitFilter fitFilter(ClientHttpRequestInterceptor clientHttpRequestInterceptor,
      RestTemplate template) {
    return new FitFilter(clientHttpRequestInterceptor, template);
  }

  // Register MVC interceptor
  @Bean
  RequestMappingHandlerMapping requestMappingHandlerMapping(
      AsyncHandlerInterceptor mvcInterceptor) {

    RequestMappingHandlerMapping r = new RequestMappingHandlerMapping();
    r.setInterceptors(mvcInterceptor);

    return r;
  }

  static class FitFilter {

    FitFilter(ClientHttpRequestInterceptor interceptor, RestTemplate restTemplate) {
      restTemplate.getInterceptors().add(0, interceptor);
    }
  }
}
