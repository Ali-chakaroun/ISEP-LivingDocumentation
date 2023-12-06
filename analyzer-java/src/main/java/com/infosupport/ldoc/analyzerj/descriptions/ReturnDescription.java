package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReturnDescription(
    @JsonProperty("Expression")
    String expression
) implements Description {

  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions";
  }
}
