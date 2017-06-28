package com.github.ervis;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Intercepts Spring VMC controllers calls.
 */
public class FailureInjectionPointInterceptor implements HandlerInterceptor {

  private final Tracing tracing;
  private final FailureServiceClient client;

  public FailureInjectionPointInterceptor(Tracing tracing, FailureServiceClient client) {
    this.tracing = tracing;
    this.client = client;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    boolean isMvcController = handler instanceof HandlerMethod;

    if (isMvcController) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      String controllerMethodFullName =
          handlerMethod.getBeanType().getName() + "." + handlerMethod.getMethod().getName();

      List<FailureResponse> baggage = client.getFailureBaggage(request, controllerMethodFullName);
      if (baggage != null) {
        addDownstream(baggage);
      }
    }

    return true;
  }

  private void addDownstream(List<FailureResponse> baggage) throws JsonProcessingException {
    Tracer tracer = tracing.tracer();
    Span currentSpan = tracer.currentSpan();

    if (currentSpan != null) {
      ObjectMapper objectMapper = new ObjectMapper();
      currentSpan.tag("failureAttack", objectMapper.writeValueAsString(baggage));
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {

  }
}
