package com.infosupport.ldoc.analyzerj.descriptions;

import java.util.Objects;

/**
 * Helper class to filter the types that need to be serialized (all excluding
 * {@link TypeType#CLASS}).
 */
public class TypeTypeFilter {

  /**
   * Filter out non-default values (so skip "Type":0).
   *
   * @param obj TypeType object
   * @return True if obj is a non-default value, False if obj is {@link TypeType#CLASS}.
   */
  @Override
  public boolean equals(Object obj) {
    return obj instanceof TypeType && Objects.equals(TypeType.CLASS, obj);
  }

  /**
   * Returns some integer. TypeTypeFilter is not intended to be used as a hash key.
   *
   * @return int 0
   */
  @Override
  public int hashCode() {
    return 0;
  }
}
