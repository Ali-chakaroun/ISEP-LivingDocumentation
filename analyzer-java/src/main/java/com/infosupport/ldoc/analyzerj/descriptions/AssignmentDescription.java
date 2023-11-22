package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description for an assigment statement. Usually contained within a list of statements, within a
 * higher level description.
 *
 * @param left     Left-hand side of the assignment as a string.
 * @param operator Assignment operator (usally '=') as a string.
 * @param right    Right-hand side of the assignment as a string.
 */
public record AssignmentDescription(
    @JsonProperty("Left")
    String left,

    @JsonProperty("Operator")
    String operator,

    @JsonProperty("Right")
    String right
) implements Description {

  /**
   * Identifies the statement as an assignment.
   *
   * @return Assignment identifier (string).
   */
  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.AssignmentDescription, LivingDocumentation.Descriptions";
  }
}
