package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import java.util.List;
import org.junit.jupiter.api.Test;

class ModifierDescriptionTest {

  @Test
  void modifier_with_no_equivalent_returns_empty_list() {
    assertIterableEquals(List.of(), ModifierDescription.of(Modifier.strictfpModifier()));
  }

  @Test
  void modifier_with_direct_equivalent_returns_equivalent() {
    assertIterableEquals(
        List.of(ModifierDescription.PRIVATE),
        ModifierDescription.of(Modifier.privateModifier()));
  }

  @Test
  void modifier_final_depends_on_context() {
    // When the Java final modifier appears on a class, the meaning is closest to C# 'sealed'.
    ClassOrInterfaceDeclaration exampleClass = new ClassOrInterfaceDeclaration(
        NodeList.nodeList(Modifier.finalModifier()), false, "Example");
    assertIterableEquals(
        List.of(ModifierDescription.SEALED),
        ModifierDescription.of(exampleClass.getModifiers().getFirst().orElseThrow()));

    // When the Java final modifier appears on a field, the meaning is closest to C# 'readonly'.
    FieldDeclaration exampleField = new FieldDeclaration(
        NodeList.nodeList(Modifier.finalModifier()),
        PrimitiveType.intType(), "someField");
    assertIterableEquals(
        List.of(ModifierDescription.READONLY),
        ModifierDescription.of(exampleField.getModifiers().getFirst().orElseThrow()));
  }
}
