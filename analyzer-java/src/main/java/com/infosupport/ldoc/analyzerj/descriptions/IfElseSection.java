package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record IfElseSection(
    @JsonProperty("Condition")
    @JsonInclude(Include.NON_NULL)
    String condition,

    @JsonProperty("Statements")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> statements
) implements Description {

}
