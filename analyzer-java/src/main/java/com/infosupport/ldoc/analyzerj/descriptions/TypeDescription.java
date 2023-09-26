package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class TypeDescription implements Description {

  @JsonProperty("FullName")
  public final String fullName;

  @JsonProperty("BaseTypes")
  @JsonInclude(Include.NON_EMPTY)
  public final List<String> baseTypes;

  public TypeDescription(String fullName, List<String> baseTypes) {
    this.fullName = fullName;
    this.baseTypes = baseTypes;
  }
}
