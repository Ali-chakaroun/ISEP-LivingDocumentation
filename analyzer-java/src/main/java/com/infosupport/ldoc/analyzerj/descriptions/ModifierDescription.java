package com.infosupport.ldoc.analyzerj.descriptions;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.util.List;

/**
 * Enumeration for different modifiers. The modifiers here are those that are defined by
 * LivingDocumentation and that have an equivalent in Java. Modifiers that only exist in C# (for
 * example 'async') or Java (like 'strictfp') are not included, but could be in the future.
 * Values are taken from <code>LivingDocumentation.Abstractions/Modifier.cs</code>.
 */
public enum ModifierDescription implements Description {
  PUBLIC(1 << 1),
  PRIVATE(1 << 2),
  PROTECTED(1 << 3),
  STATIC(1 << 4),
  ABSTRACT(1 << 5),
  READONLY(1 << 7), /* Java: final (when referring to fields or variables) */
  SEALED(1 << 10), /* Java: final (when referring to classes) */
  EXTERN(1 << 12); /* Java: native */

  private final int value;

  ModifierDescription(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static List<Description> of(Modifier modifier) {
    boolean onType = modifier.getParentNode().map(n -> n instanceof TypeDeclaration).orElse(false);

    return switch (modifier.getKeyword()) {
      case PUBLIC -> List.of(PUBLIC);
      case PROTECTED -> List.of(PROTECTED);
      case PRIVATE -> List.of(PRIVATE);
      case ABSTRACT -> List.of(ABSTRACT);
      case STATIC -> List.of(STATIC);
      case FINAL -> onType ? List.of(SEALED) : List.of(READONLY);
      case NATIVE -> List.of(EXTERN);
      default -> List.of();
    };
  }
}
