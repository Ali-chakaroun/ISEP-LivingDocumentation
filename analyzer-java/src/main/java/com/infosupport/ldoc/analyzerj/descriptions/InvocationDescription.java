package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description of an Invocation statement.
 *
 * @param containingType The class of the invoked method (string).
 * @param name           The name of the invoked method (string).
 * @param arguments      A list of {@link ArgumentDescription}, containing the arguments given to
 *                       the invoked method.
 */
public record InvocationDescription(
    @JsonProperty("ContainingType")
    String containingType,

    @JsonProperty("Name")
    String name,

    @JsonProperty("Arguments")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> arguments
) implements Description {

  /**
   * Identifies the statement as an invocation.
   *
   * @return Invocation identifier (string).
   */
  @JsonProperty(value = "$type", index = -2)
  public String type() {
    return "LivingDocumentation.InvocationDescription, LivingDocumentation.Descriptions";
  }
}
