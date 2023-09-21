package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AttributeDescription(
    @JsonProperty("Type")
    String type,

    @JsonProperty("Name")
    String name,

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("Arguments")
    List<Description> arguments
) implements Description {

}
