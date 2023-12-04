package com.infosupport.ldoc.analyzerj.descriptions;

import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * Enumeration for different modifiers. The modifiers here are those that are defined by
 * LivingDocumentation and that have an equivalent in Java. Modifiers that only exist in C# (for
 * example 'async') or Java (like 'strictfp') are not included, but could be in the future. Values
 * (bitmasks) are taken from <code>LivingDocumentation.Abstractions/Modifier.cs</code>.
 */
public enum Modifier {
  NONE(0),
  PUBLIC(1 << 1),
  PRIVATE(1 << 2),
  PROTECTED(1 << 3),
  STATIC(1 << 4),
  ABSTRACT(1 << 5),
  READONLY(1 << 7), /* Java: final (when referring to fields or variables) */
  SEALED(1 << 10), /* Java: final (when referring to classes) */
  EXTERN(1 << 12); /* Java: native */

  /**
   * Integer that is a power of 2, unique for each modifier.
   */
  private final int mask;

  /**
   * Constructor.
   *
   * @param mask The mask of the enum constant.
   */
  Modifier(int mask) {
    this.mask = mask;
  }

  /**
   * Get the mask of an enum constant.
   *
   * @return mask (int).
   */
  public int mask() {
    return mask;
  }

  /**
   * Converts a Javaparser ast.Modifier to our definition, if such a conversion exists.
   *
   * @param  modifier A modifier keyword from a Javaparser Abstract Syntax Tree.
   * @return          a Modifier value or {@link Modifier#NONE} if there is no equivalent.
   */
  public static Modifier valueOf(com.github.javaparser.ast.Modifier modifier) {
    boolean onType = modifier.getParentNode().map(TypeDeclaration.class::isInstance).orElse(false);

    return switch (modifier.getKeyword()) {
      case PUBLIC -> PUBLIC;
      case PROTECTED -> PROTECTED;
      case PRIVATE -> PRIVATE;
      case ABSTRACT -> ABSTRACT;
      case STATIC -> STATIC;
      case FINAL -> onType ? SEALED : READONLY;
      case NATIVE -> EXTERN;
      default -> NONE;
    };
  }
}
