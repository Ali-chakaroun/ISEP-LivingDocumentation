package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description of a 'for each' statement. Meant to be contained within a list of statements, within a
 * higher level description. Note that as this description contains a list of statements itself, it
 * can be nested.
 *
 * @param expression The iteration statement (string).
 * @param statements A list of statements from the loop body.
 */
public record ForEachDescription(
    @JsonProperty("Expression")
    String expression,

    @JsonProperty("Statements")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> statements
) implements Description {

  /**
   * Identifies the statement as a For Each statement.
   *
   * @return ForEach identifier (string).
   */
  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.ForEach, LivingDocumentation.Statements";
  }
}
