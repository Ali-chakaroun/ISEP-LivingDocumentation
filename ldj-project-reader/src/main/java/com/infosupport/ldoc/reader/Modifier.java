package com.infosupport.ldoc.reader;

/**
 * Enumeration of modifiers. Contains all modifiers that can be found in Living Documentation JSON.
 *
 * @see HasModifiers
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
   * Unique bitmask with the bit set that corresponds to the modifier.
   */
  private final long mask;

  /**
   * Modifier constructor. Sets mask to given number.
   *
   * @param i Bitmask value.
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
