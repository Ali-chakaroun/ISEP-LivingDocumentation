package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description of a switch statement.
 *
 * @param expression The switch expression, or switch variable (string).
 * @param sections   A list of {@link SwitchSection}, one for each case in the switch.
 */
public record SwitchDescription(
    @JsonProperty("Expression")
    String expression,

    @JsonProperty("Sections")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> sections
) implements Description {

  /**
   * Identifies the statement as a switch.
   *
   * @return Switch identifier (string).
   */
  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.Switch, LivingDocumentation.Statements";
  }
}
