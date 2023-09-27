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

  @JsonProperty("$type")
  public String getType() {
    return "LivingDocumentation.AssignmentDescription, LivingDocumentation.Descriptions";
  }
}
