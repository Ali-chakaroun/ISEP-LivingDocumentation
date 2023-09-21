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
    List<String> baseTypes
) implements Description {

}
