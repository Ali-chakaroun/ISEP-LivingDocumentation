package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record FieldDescription(

    @JsonUnwrapped
    MemberDescription member,

    @JsonProperty("Type")
    String type,

    @JsonProperty("Initializer")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String initialValue,

    @JsonProperty("DocumentationComments")
    Description comments

) implements Description {

}
