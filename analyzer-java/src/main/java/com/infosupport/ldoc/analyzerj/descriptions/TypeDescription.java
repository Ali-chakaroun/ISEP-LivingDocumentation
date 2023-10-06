package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TypeDescription(
    @JsonProperty("Type")
    @JsonInclude(value = Include.CUSTOM, valueFilter = TypeTypeFilter.class)
    @JsonFormat(shape = Shape.NUMBER)
    TypeType type,

    @JsonProperty("FullName")
    String fullName,

    @JsonProperty("BaseTypes")
    @JsonInclude(Include.NON_EMPTY)
    List<String> baseTypes,
    @JsonProperty("DocumentationComments")
    @JsonInclude(Include.NON_EMPTY)
    Description comments,
    @JsonProperty("Constructors")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> constructors,

    @JsonProperty("Methods")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> methods,

    @JsonProperty("Attributes")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> attributes
) implements Description {

  public TypeDescription(TypeType type, String fullName) {
    this(type, fullName, List.of());
  }

  public TypeDescription(TypeType type, String fullName, List<String> baseTypes) {
    this(type, fullName, baseTypes, null, List.of(), List.of(), List.of());
  }

}
