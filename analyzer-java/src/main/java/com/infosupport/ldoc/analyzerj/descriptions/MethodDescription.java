package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.List;

/**
 * Description for a method. Usually contained in a list of methods, within a TypeDescription.
 *
 * @param member     {@link MemberDescription}: the name, modifiers and annotations of the method.
 * @param returnType Type of the return value of the method (string).
 * @param comments   JavaDoc comments in an {@link CommentSummaryDescription}.
 * @param parameters List of method {@link ParameterDescription}.
 * @param statements List of statements from the method body.
 */
public record MethodDescription(
    @JsonUnwrapped
    MemberDescription member,
    @JsonProperty("ReturnType")
    String returnType,
    @JsonProperty("DocumentationComments")
    @JsonInclude(Include.NON_EMPTY)
    Description comments,
    @JsonProperty("Parameters")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> parameters,

    @JsonProperty("Statements")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> statements
) implements Description {
}
