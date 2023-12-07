package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description of a single annotation (called attribute by .NET terminology).
 *
 * @param type      Type of the annotation (string)
 * @param name      Name of the annotation (string)
 * @param arguments List of {@link ArgumentDescription}, representing the arguments given to the
 *                  annotation.
 */
public record AttributeDescription(
    @JsonProperty("Type")
    String type,

    @JsonProperty("Name")
    String name,

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("Arguments")
    List<Description> arguments
) implements Description {

}
