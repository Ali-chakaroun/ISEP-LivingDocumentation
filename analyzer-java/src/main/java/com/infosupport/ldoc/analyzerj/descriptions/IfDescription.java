package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description for an if statement, including the if else and else clauses. Note that since the
 * clauses can contain a list of statements again, this can be nested.
 *
 * @param sections A list of {@link IfElseSection}, containing the if, if else and else clauses.
 */
public record IfDescription(
    @JsonProperty("Sections")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> sections
) implements Description {

  /**
   * Identifies the statement as an If statement.
   *
   * @return Assignment identifier (string).
   */
  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.If, LivingDocumentation.Statements";
  }
}
