package com.infosupport.ldoc.analyzerj.renderextensions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infosupport.ldoc.analyzerj.descriptions.ArgumentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.InvocationDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MemberDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ParameterDescription;
import java.util.List;
import org.junit.jupiter.api.Test;

public class InvocationDescriptionExtensionsTest {

  @Test
  public void matchesMethod_NullMethod_ShouldThrow() {
    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of());

    assertThrows(IllegalArgumentException.class,
        () -> InvocationDescriptionExtensions.invocationMatchesMethod(invocation, null));
  }

  @Test
  public void matchesMethod_NullInvocation_ShouldThrow() {
    MethodDescription method = new MethodDescription(new MemberDescription("Method"), "void",
        List.of(), List.of());

    assertThrows(IllegalArgumentException.class,
        () -> InvocationDescriptionExtensions.invocationMatchesMethod(null, method));
  }

  @Test
  public void matchesMethod_InvocationWithoutArguments_And_MethodWithoutParameters_ShouldMatch() {
    MethodDescription method = new MethodDescription(new MemberDescription("Method"), "void",
        List.of(), List.of());
    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of());

    assertTrue(InvocationDescriptionExtensions.invocationMatchesMethod(invocation, method));
  }


  @Test
  public void matchesMethod_InvocationNameEqualsMethodName_ShouldMatch() {
    MethodDescription method = new MethodDescription(new MemberDescription("Method"), "void",
        List.of(), List.of());
    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of());

    assertTrue(InvocationDescriptionExtensions.invocationMatchesMethod(invocation, method));
  }

  @Test
  public void matchesMethod_InvocationNameNotEqualsMethodName_Should_NotMatch() {
    MethodDescription method = new MethodDescription(new MemberDescription("OtherMethod"), "void",
        List.of(), List.of());
    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of());

    assertFalse(InvocationDescriptionExtensions.invocationMatchesMethod(invocation, method));
  }

  @Test
  public void matchesParameters_NullMethod_ShouldThrow() {
    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of());

    assertThrows(IllegalArgumentException.class,
        () -> InvocationDescriptionExtensions.invocationMatchesMethodParameters(invocation, null));
  }

  @Test
  public void matchesParameters_NullInvocation_ShouldThrow() {
    MethodDescription method = new MethodDescription(new MemberDescription("Method"), "void",
        List.of(), List.of());

    assertThrows(IllegalArgumentException.class,
        () -> InvocationDescriptionExtensions.invocationMatchesMethodParameters(null, method));
  }

  @Test
  public void matchesParameters_InvocationWithoutArguments_And_MethodWithoutParameters_ShouldMatch() {
    MethodDescription method = new MethodDescription(new MemberDescription("Method"), "void",
        List.of(), List.of());
    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of());

    assertTrue(
        InvocationDescriptionExtensions.invocationMatchesMethodParameters(invocation, method));
  }

  @Test
  public void matchesParameters_InvocationWithNoArguments_And_MethodWithParameters_Should_NotMatch() {
    MethodDescription method = new MethodDescription(
        new MemberDescription("Method"),
        "void",
        List.of(
            new ParameterDescription("string", "parameter1", List.of())),
        List.of());
    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of());

    assertFalse(
        InvocationDescriptionExtensions.invocationMatchesMethodParameters(invocation, method));
  }

  @Test
  public void matchesParameters_InvocationWithArguments_And_MethodWithMoreParameters_Should_NotMatch() {
    MethodDescription method = new MethodDescription(
        new MemberDescription("Method"),
        "void",
        List.of(
            new ParameterDescription("string", "parameter1", List.of()),
            new ParameterDescription("string", "parameter2", List.of())),
        List.of());

    InvocationDescription invocation = new InvocationDescription("java.lang.Object", "Method",
        List.of(new ArgumentDescription("string", "argument1")));

    assertFalse(
        InvocationDescriptionExtensions.invocationMatchesMethodParameters(invocation, method));
  }
}