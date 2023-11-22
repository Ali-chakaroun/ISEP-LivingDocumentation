package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description of a return statement.
 *
 * @param expression The text of the return statement (string). This can be a constant, expression,
 *                   variable, etc.
 */
public record ReturnDescription(
    @JsonProperty("Expression")
    @JsonInclude(Include.NON_NULL)
    String expression
) implements Description {

  public ReturnDescription() {
    this(null);
  }

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
