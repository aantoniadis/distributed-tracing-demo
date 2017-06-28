package com.github.ervis;

import java.util.List;

public class FailureResponse {

  private String name;
  private String failureType;
  private List<String> injectionPoints;

  public FailureResponse() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFailureType() {
    return failureType;
  }

  public void setFailureType(String failureType) {
    this.failureType = failureType;
  }

  public List<String> getInjectionPoints() {
    return injectionPoints;
  }

  public void setInjectionPoints(List<String> injectionPoints) {
    this.injectionPoints = injectionPoints;
  }
}
