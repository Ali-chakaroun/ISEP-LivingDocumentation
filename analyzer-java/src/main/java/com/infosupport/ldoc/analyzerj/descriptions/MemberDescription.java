package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record MemberDescription(
    @JsonProperty("Name")
    String name,

    @JsonProperty("Modifiers")
    @JsonInclude(Include.NON_DEFAULT)
    int modifiers,

    @JsonProperty("Attributes")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> attributes,

    @JsonProperty("DocumentationComments")
    @JsonInclude(Include.NON_NULL)
    Description comments
) implements Description {

  public MemberDescription(String name) {
    this(name, 0, List.of(), null);
  }
}
