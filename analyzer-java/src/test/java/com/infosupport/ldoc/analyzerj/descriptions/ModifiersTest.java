package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import org.junit.jupiter.api.Test;

class ModifiersTest {

  @Test
  void modifier_with_no_equivalent_returns_none() {
    assertEquals(
        Modifiers.NONE,
        Modifiers.valueOf(Modifier.strictfpModifier()));
  }

  @Test
  void modifier_with_direct_equivalent_returns_equivalent() {
    assertEquals(
        Modifiers.PRIVATE,
        Modifiers.valueOf(Modifier.privateModifier()));
  }

  @Test
  void modifier_final_depends_on_context() {
    // When the Java final modifier appears on a class, the meaning is closest to C# 'sealed'.
    ClassOrInterfaceDeclaration exampleClass = new ClassOrInterfaceDeclaration(
        NodeList.nodeList(Modifier.finalModifier()), false, "Example");
    assertEquals(
        Modifiers.SEALED,
        Modifiers.valueOf(exampleClass.getModifiers().getFirst().orElseThrow()));

    // When the Java final modifier appears on a field, the meaning is closest to C# 'readonly'.
    FieldDeclaration exampleField = new FieldDeclaration(
        NodeList.nodeList(Modifier.finalModifier()),
        PrimitiveType.intType(), "someField");
    assertEquals(
        Modifiers.READONLY,
        Modifiers.valueOf(exampleField.getModifiers().getFirst().orElseThrow()));
  }
}
