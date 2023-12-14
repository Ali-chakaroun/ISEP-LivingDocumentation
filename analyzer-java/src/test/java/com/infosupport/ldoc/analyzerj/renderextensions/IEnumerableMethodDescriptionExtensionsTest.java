package com.infosupport.ldoc.analyzerj.renderextensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.infosupport.ldoc.analyzerj.descriptions.MemberDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IEnumerableMethodDescriptionExtensionsTest {

  List<MethodDescription> list1233;

  @BeforeEach
  void fillList() {
    list1233 = new ArrayList<>();
    list1233.add(
        new MethodDescription(new MemberDescription("Method1"), "void", List.of(), List.of()));
    list1233.add(
        new MethodDescription(new MemberDescription("Method2"), "void", List.of(), List.of()));
    list1233.add(
        new MethodDescription(new MemberDescription("Method3"), "void", List.of(), List.of()));
    list1233.add(
        new MethodDescription(new MemberDescription("Method3"), "void", List.of(), List.of()));
  }

  @DisplayName("When the list is null, give an error.")
  @Test
  public void extensionMethodShouldGuardAgainstNRE() {
    assertThrows(IllegalArgumentException.class,
        () -> IEnumerableMethodDescriptionExtensions.methodsWithName(null, ""));
  }

  @DisplayName("When the method exists in the list, return the exact match")
  @Test
  public void expectTheFilterToReturnExactMatch() {
    List<MethodDescription> result = IEnumerableMethodDescriptionExtensions.methodsWithName(
        list1233,
        "Method1");

    assertEquals(1, result.size());
    assertEquals("Method1", result.get(0).member().name());
  }

  @DisplayName("When the method exists multiple times in the list, return all matches")
  @Test
  public void expectTheFilterToReturnMultipleMatches() {
    List<MethodDescription> result = IEnumerableMethodDescriptionExtensions.methodsWithName(
        list1233,
        "Method3");

    assertEquals(2, result.size());

    for (MethodDescription methodDescription : result) {
      assertEquals("Method3", methodDescription.member().name());
    }
  }

  @DisplayName("When the method does not exist in the list, return no matches")
  @Test
  public void expectNoMatchesForNonexistentMethod() {
    List<MethodDescription> result = IEnumerableMethodDescriptionExtensions.methodsWithName(
        list1233,
        "Method4");

    assertEquals(0, result.size());
  }

  @DisplayName("When the method exists with a different casing in the list, return no matches")
  @Test
  public void expectNoMatchesForDifferentCasing() {
    List<MethodDescription> result = IEnumerableMethodDescriptionExtensions.methodsWithName(
        list1233,
        "method1");

    assertEquals(0, result.size());
  }
}

