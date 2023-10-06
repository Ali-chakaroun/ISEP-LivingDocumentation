package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ForEachDescription(
    @JsonProperty("Expression")
    String expression,

    @JsonProperty("Statements")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> statements
) implements Description {

  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.ForEach, LivingDocumentation.Statements";
  }
}
