package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description for an argument of an Annotation (Attribute) ({@link AttributeDescription}). Note
 * this is not the same as a general argument ({@link ArgumentDescription}).
 *
 * @param name  Name of the annotation parameter (string)
 * @param type  Type of the argument value (string)
 * @param value Argument value as a string. This can be a constant, expression, variable, etc.
 */
public record AttributeArgumentDescription(
    @JsonProperty("Name")
    String name,

    @JsonProperty("Type")
    String type,

    @JsonProperty("Value")
    String value
) implements Description {

}
