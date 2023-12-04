package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description for an if, if else or else clause. Meant to be contained within a list of
 * IfElseSections, within a {@link IfDescription}.
 *
 * @param condition  The conditional expression of the clause (null for an else clause) (string).
 * @param statements A list of statements from the clause body.
 */
public record IfElseSection(
    @JsonProperty("Condition")
    @JsonInclude(Include.NON_NULL)
    String condition,

    @JsonProperty("Statements")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> statements
) implements Description {

}
