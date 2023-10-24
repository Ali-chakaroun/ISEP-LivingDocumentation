package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record CommentSummaryDescription(
    @JsonProperty("Remarks")
    @JsonInclude(Include.NON_EMPTY)
    String remarks,
    @JsonProperty("Returns")
    @JsonInclude(Include.NON_EMPTY)
    String returns,
    @JsonProperty("Summary")
    @JsonInclude(Include.NON_EMPTY)
    String summary,

    @JsonProperty("Params")
    @JsonInclude(Include.NON_EMPTY)
    Map<String, String> params,
    @JsonProperty("TypeParams")
    @JsonInclude(Include.NON_EMPTY)
    Map<String, String> typeParams

) implements Description {

}
