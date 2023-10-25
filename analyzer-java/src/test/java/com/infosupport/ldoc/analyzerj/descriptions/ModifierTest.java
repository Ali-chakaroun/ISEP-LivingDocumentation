package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import org.junit.jupiter.api.Test;

class ModifierTest {

  @Test
  void modifier_with_public_equivalent_returns_equivalent() {

  }
  @Test
  void modifier_with_no_equivalent_returns_none() {
    assertEquals(
        Modifier.NONE,
        Modifier.valueOf(com.github.javaparser.ast.Modifier.strictfpModifier()));
  }

  @Test
  void modifier_with_direct_equivalent_returns_equivalent() {
    assertEquals(
        Modifier.PUBLIC,
        Modifier.valueOf(com.github.javaparser.ast.Modifier.publicModifier()));
    assertEquals(
        Modifier.PRIVATE,
        Modifier.valueOf(com.github.javaparser.ast.Modifier.privateModifier()));
    assertEquals(
        Modifier.PROTECTED,
        Modifier.valueOf(com.github.javaparser.ast.Modifier.protectedModifier()));
    assertEquals(
        Modifier.ABSTRACT,
        Modifier.valueOf(com.github.javaparser.ast.Modifier.abstractModifier()));
    assertEquals(
        Modifier.STATIC,
        Modifier.valueOf(com.github.javaparser.ast.Modifier.staticModifier()));
    assertEquals(
        Modifier.EXTERN,
        Modifier.valueOf(com.github.javaparser.ast.Modifier.nativeModifier()));
  }

  @Test
  void modifier_final_depends_on_context() {
    // When the Java final modifier appears on a class, the meaning is closest to C# 'sealed'.
    ClassOrInterfaceDeclaration exampleClass = new ClassOrInterfaceDeclaration(
        NodeList.nodeList(com.github.javaparser.ast.Modifier.finalModifier()), false, "Example");
    assertEquals(
        Modifier.SEALED,
        Modifier.valueOf(exampleClass.getModifiers().getFirst().orElseThrow()));

    // When the Java final modifier appears on a field, the meaning is closest to C# 'readonly'.
    FieldDeclaration exampleField = new FieldDeclaration(
        NodeList.nodeList(com.github.javaparser.ast.Modifier.finalModifier()),
        PrimitiveType.intType(), "someField");
    assertEquals(
        Modifier.READONLY,
        Modifier.valueOf(exampleField.getModifiers().getFirst().orElseThrow()));
  }
}
