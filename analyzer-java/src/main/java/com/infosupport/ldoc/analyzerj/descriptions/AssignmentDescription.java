package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AssignmentDescription(
    @JsonProperty("Left")
    String left,

    @JsonProperty("Operator")
    String operator,

    @JsonProperty("Right")
    String right
) implements Description {

  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.AssignmentDescription, LivingDocumentation.Descriptions";
  }
}
