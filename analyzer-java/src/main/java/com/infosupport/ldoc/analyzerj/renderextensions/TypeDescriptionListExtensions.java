package com.infosupport.ldoc.analyzerj.renderextensions;

import com.infosupport.ldoc.analyzerj.descriptions.ConstructorDescription;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.InvocationDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import com.infosupport.ldoc.analyzerj.descriptions.TypeDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class TypeDescriptionListExtensions {

  public static TypeDescription first(List<TypeDescription> types, String typeName) {
    if (types == null) {
      throw new IllegalArgumentException("Types cannot be null");
    }

    Optional<TypeDescription> firstType = types.stream()
        .filter(type -> type.fullName().equals(typeName))
        .findFirst();

    return firstType.orElseThrow();
  }

  public static TypeDescription firstOrDefault(List<TypeDescription> types, String typeName) {
    if (types == null) {
      throw new IllegalArgumentException("Types cannot be null");
    }

    return types.stream().filter(type -> type.fullName().equals(typeName))
        .findFirst()
        .orElse(null);
  }

  public static List<Description> getInvokedMethod(List<TypeDescription> types,
      InvocationDescription invocation) {
    if (types == null) {
      throw new IllegalArgumentException("Types cannot be null");
    }

    TypeDescription type = firstOrDefault(types, invocation.containingType());
    if (type == null) {
      return new ArrayList<>();
    }

    List matchingConstructors = type.constructors().stream()
        .filter(constructor -> InvocationDescriptionExtensions.invocationMatchesMethod(invocation,
            constructor))
        .toList();
    List matchingMethods = type.constructors().stream()
        .filter(
            method -> InvocationDescriptionExtensions.invocationMatchesMethod(invocation, method))
        .toList();

    return Stream.concat(matchingConstructors.stream(), matchingMethods.stream()).toList();
  }

  public static List<InvocationDescription> getInvocationConsequences(List<TypeDescription> types,
      InvocationDescription invocation) {
    if (types == null) {
      throw new IllegalArgumentException("Types cannot be null");
    }

    List invoked = getInvokedMethod(types, invocation);
    List<MethodDescription> invokedMethods = new ArrayList<>();
    List<ConstructorDescription> invokedConstructors = new ArrayList<>();
    for (Object o : invoked) {
      if (o instanceof ConstructorDescription) {
        invokedConstructors.add((ConstructorDescription) o);
      } else if (o instanceof MethodDescription) {
        invokedMethods.add((MethodDescription) o);
      }
    }

    List<InvocationDescription> consequencesMethods = invokedMethods.stream()
        .flatMap(method -> (method).statements().stream()
            .filter(statement -> statement instanceof InvocationDescription)
            .map(im -> (InvocationDescription) im)
            .flatMap(im -> getInvocationConsequences(types, im).stream()))
        .toList();

    List<InvocationDescription> consequencesConstructors = invokedConstructors.stream()
        .flatMap(constructor -> (constructor).statements().stream()
            .filter(statement -> statement instanceof InvocationDescription)
            .map(im -> (InvocationDescription) im)
            .flatMap(im -> getInvocationConsequences(types, im).stream()))
        .toList();

    return Stream.concat(Stream.of(invocation),
        Stream.concat(consequencesMethods.stream(), consequencesConstructors.stream())).toList();
  }

  public static List<String> getInvocationConsequenceStatements(List<TypeDescription> types,
      InvocationDescription invocation)
      throws NotImplementedException {
    //TODO, statements? Don't think we have those
    throw new NotImplementedException("getInvocationConsequenceStatements");
  }

  private static List<String> traverseStatement(Iterable<TypeDescription> types, String statement)
      throws NotImplementedException {
    //TODO, statements? Don't think we have those
    throw new NotImplementedException("traverseStatement");
  }

  //TODO:
  //populate inherited base types x2
  //populate inherited members
}
