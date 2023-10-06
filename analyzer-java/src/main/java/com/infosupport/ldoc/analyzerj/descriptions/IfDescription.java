package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record IfDescription(
    @JsonProperty("Sections")
    @JsonInclude(Include.NON_EMPTY)
    List<IfElseSection> sections
) implements Description {

  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.If, LivingDocumentation.Statements";
  }
}
