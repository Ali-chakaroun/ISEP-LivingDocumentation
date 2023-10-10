package com.infosupport.ldoc.analyzerj.descriptions;

import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * Enumeration for different modifiers. The modifiers here are those that are defined by
 * LivingDocumentation and that have an equivalent in Java. Modifiers that only exist in C# (for
 * example 'async') or Java (like 'strictfp') are not included, but could be in the future. Values
 * (bitmasks) are taken from <code>LivingDocumentation.Abstractions/Modifier.cs</code>.
 */
public enum Modifiers {
  NONE(0),
  PUBLIC(1 << 1),
  PRIVATE(1 << 2),
  PROTECTED(1 << 3),
  STATIC(1 << 4),
  ABSTRACT(1 << 5),
  READONLY(1 << 7), /* Java: final (when referring to fields or variables) */
  SEALED(1 << 10), /* Java: final (when referring to classes) */
  EXTERN(1 << 12); /* Java: native */

  private final int mask;

  Modifiers(int mask) {
    this.mask = mask;
  }

  public int mask() {
    return mask;
  }

  public static Modifiers valueOf(com.github.javaparser.ast.Modifier modifier) {
    boolean onType = modifier.getParentNode().map(n -> n instanceof TypeDeclaration).orElse(false);

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
