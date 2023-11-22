package com.infosupport.ldoc.analyzerj.descriptions;

import java.util.Objects;

/**
 * I don't know exactly what this is for ?.
 */
public class TypeTypeFilter {

  /**
   * Check if given object is a TypeType.CLASS ?.
   *
   * @param obj ?
   * @return ?
   */
  @Override
  public boolean equals(Object obj) {
    return obj instanceof TypeType && Objects.equals(TypeType.CLASS, obj);
  }

  /**
   * ?.
   *
   * @return int 0
   */
  @Override
  public int hashCode() {
    return 0;
  }
}
