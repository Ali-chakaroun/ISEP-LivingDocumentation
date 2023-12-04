package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description for a single argument. Meant to be contained within a list of arguments.
 *
 * @param type Type of the argument (string)
 * @param text Argument value as a string. This can be a constant, expression, variable, etc.
 */
public record ArgumentDescription(
    @JsonProperty("Type")
    String type,

    @JsonProperty("Text")
    String text
) implements Description {

}
