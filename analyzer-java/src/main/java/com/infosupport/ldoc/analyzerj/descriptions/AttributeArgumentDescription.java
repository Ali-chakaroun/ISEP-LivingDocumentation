package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AttributeArgumentDescription(
    @JsonProperty("Name")
    String name,

    @JsonProperty("Type")
    String type,

    @JsonProperty("Value")
    String value
) implements Description {

}
