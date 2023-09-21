package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ParameterDescription(
    @JsonProperty("Type")
    String type,

    @JsonProperty("Name")
    String name
) implements Description {

}
