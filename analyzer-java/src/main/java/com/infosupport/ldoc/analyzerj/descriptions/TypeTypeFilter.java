package com.infosupport.ldoc.analyzerj.descriptions;

import java.util.Objects;

public class TypeTypeFilter {

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TypeType && Objects.equals(TypeType.CLASS, obj);
  }
}
