package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberDescription(
    @JsonProperty("Name")
    String name
) implements Description {

}
