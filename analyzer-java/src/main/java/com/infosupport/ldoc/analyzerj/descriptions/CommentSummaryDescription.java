package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record CommentSummaryDescription(
        @JsonProperty("Summary")
        @JsonInclude(Include.NON_EMPTY)
        String summary,
        @JsonProperty("params")
        @JsonInclude(Include.NON_EMPTY)
        Map<String, String> params,
        @JsonProperty("TypeParams")
        @JsonInclude(Include.NON_EMPTY)
        Map<String, String> TypeParams

) implements Description {
}
