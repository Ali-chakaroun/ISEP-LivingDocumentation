package com.infosupport.ldoc.reader;

/**
 * Enumeration of modifiers. Contains all modifiers that can be found in the JSON file.
 */
public enum Modifier {
  INTERNAL(1),
  PUBLIC(1 << 1),
  PRIVATE(1 << 2),
  PROTECTED(1 << 3),
  STATIC(1 << 4),
  ABSTRACT(1 << 5),
  OVERRIDE(1 << 6),
  READONLY(1 << 7),
  ASYNC(1 << 8),
  CONST(1 << 9),
  SEALED(1 << 10),
  VIRTUAL(1 << 11),
  EXTERN(1 << 12),
  NEW(1 << 13),
  UNSAFE(1 << 14),
  PARTIAL(1 << 15);

  /**
   * Integer that is a power of 2, unique for each modifier.
   */
  private final long mask;

  /**
   * Modifier constructor. Sets mask to given number.
   *
   * @param i Mask number.
   */
  Modifier(long i) {
    mask = i;
  }

  /**
   * Get mask value.
   *
   * @return mask value.
   */
  public long mask() {
    return mask;
  }
}
