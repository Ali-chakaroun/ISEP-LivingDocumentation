package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public record EnumMemberDescription (
    @JsonUnwrapped
    MemberDescription member,
    @JsonProperty("Arguments")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Description> arguments,

    @JsonProperty("DocumentationComments")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Description comments

) implements Description {




}
