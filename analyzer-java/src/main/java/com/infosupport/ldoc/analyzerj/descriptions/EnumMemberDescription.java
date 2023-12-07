package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.List;

/**
 * Description for an enum constant.
 *
 * @param member    {@link MemberDescription}: the name, modifiers and annotations of the
 *                  constructor.
 * @param arguments List of {@link ArgumentDescription}, containing the constructor arguments (if
 *                  relevant).
 * @param comments  JavaDoc comments in an {@link CommentSummaryDescription}.
 */
public record EnumMemberDescription(
    @JsonUnwrapped
    MemberDescription member,
    @JsonProperty("Arguments")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Description> arguments

) implements Description {

}
