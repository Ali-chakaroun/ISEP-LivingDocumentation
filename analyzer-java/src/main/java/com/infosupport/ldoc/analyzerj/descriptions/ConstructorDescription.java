package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.List;

public record ConstructorDescription(
    @JsonUnwrapped
    MemberDescription member,

    @JsonProperty("Parameters")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> parameters,

    @JsonProperty("Statements")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> statements
) implements Description {

}
