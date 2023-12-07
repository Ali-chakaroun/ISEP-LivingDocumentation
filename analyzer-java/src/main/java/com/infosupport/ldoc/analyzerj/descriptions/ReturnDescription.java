package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description of a return statement.
 *
 * @param expression The text of the return statement (string). This can be a constant, expression,
 *                   variable, etc. Can be empty string for empty returns.
 */
public record ReturnDescription(
    @JsonProperty("Expression")
    String expression
) implements Description {

  /**
   * Identifies the statement as a return statement.
   *
   * @return Return identifier (string).
   */
  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions";
  }
}
