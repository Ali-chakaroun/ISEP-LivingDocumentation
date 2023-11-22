package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description for a single parameter. Usually contained in a list of parameters, in a method or
 * constructor.
 *
 * @param type       Type of the parameter (string).
 * @param name       Name of the parameter (string).
 * @param attributes List of parameter annotations ({@link AttributeDescription}).
 */
public record ParameterDescription(
    @JsonProperty("Type")
    String type,

    @JsonProperty("Name")
    String name,

    @JsonProperty("Attributes")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> attributes
) implements Description {

}
