package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Description for a JavaDoc comment.
 *
 * @param remarks    The first sentence of the JavaDoc comment?
 * @param returns    The text in the '@return' tag.
 * @param summary    The second sentence of the JavaDoc comment?
 * @param params     A mapping of parameter names to their text, from the '@param' tags.
 * @param typeParams A mapping of type parameter names to their text, from the '@param' tags that
 *                   start with '<someType>'.
 */
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
