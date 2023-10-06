package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ReturnDescription(
    @JsonProperty("Expression")
    @JsonInclude(Include.NON_NULL)
    String expression
) implements Description {

  public ReturnDescription() {
    this(null);
  }

  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions";
  }
}
