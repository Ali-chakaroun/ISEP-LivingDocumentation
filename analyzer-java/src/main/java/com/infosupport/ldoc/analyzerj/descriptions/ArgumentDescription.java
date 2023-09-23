package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArgumentDescription(
    @JsonProperty("Type")
    String type,

    @JsonProperty("Text")
    String text
) implements Description {

}
