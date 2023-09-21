package com.infosupport.ldoc.analyzerj;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.types.ResolvedType;
import com.infosupport.ldoc.analyzerj.descriptions.ConstructorDescription;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.MemberDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ParameterDescription;
import com.infosupport.ldoc.analyzerj.descriptions.TypeDescription;
import com.infosupport.ldoc.analyzerj.descriptions.TypeType;
import java.util.ArrayList;
import java.util.List;

public class AnalysisVisitor extends GenericListVisitorAdapter<Description, Analyzer> {

  private final SymbolResolver resolver;

  public AnalysisVisitor(SymbolResolver resolver) {
    this.resolver = resolver;
  }

  private String resolve(Type type) {
    return resolver.toResolvedType(type, ResolvedType.class).describe();
  }

  private List<String> resolve(List<ClassOrInterfaceType> types) {
    return types.stream().map(this::resolve).toList();
  }

  private <T extends Node> List<Description> methods(NodeWithMembers<T> n, Analyzer arg) {
    List<Description> methods = new ArrayList<>();
    for (MethodDeclaration method : n.getMethods()) {
      methods.addAll(method.accept(this, arg));
    }
    return methods;
  }

  private <T extends Node> List<Description> ctors(NodeWithMembers<T> n, Analyzer arg) {
    List<Description> constructors = new ArrayList<>();
    for (ConstructorDeclaration constructor : n.getConstructors()) {
      constructors.addAll(constructor.accept(this, arg));
    }
    return constructors;
  }

  private <T extends Node> List<Description> parameters(NodeWithParameters<T> n, Analyzer arg) {
    List<Description> parameters = new ArrayList<>();
    for (Parameter parameter : n.getParameters()) {
      parameters.addAll(parameter.accept(this, arg));
    }
    return parameters;
  }

  @Override
  public List<Description> visit(ClassOrInterfaceDeclaration n, Analyzer arg) {
    TypeType type = n.isInterface() ? TypeType.INTERFACE : TypeType.CLASS;
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = new ArrayList<>();
    baseTypes.addAll(resolve(n.getExtendedTypes()));
    baseTypes.addAll(resolve(n.getImplementedTypes()));

    return List.of(new TypeDescription(type, fullName, baseTypes, ctors(n, arg), methods(n, arg)));
  }

  @Override
  public List<Description> visit(RecordDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = resolve(n.getImplementedTypes());

    return List.of(
        new TypeDescription(TypeType.STRUCT, fullName, baseTypes, ctors(n, arg), methods(n, arg)));
  }

  @Override
  public List<Description> visit(EnumDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = resolve(n.getImplementedTypes());

    return List.of(
        new TypeDescription(TypeType.ENUM, fullName, baseTypes, ctors(n, arg), methods(n, arg)));
  }

  @Override
  public List<Description> visit(MethodDeclaration n, Analyzer arg) {
    String name = n.getNameAsString();
    String retType = resolve(n.getType());

    return List.of(
        new MethodDescription(new MemberDescription(name), retType, parameters(n, arg), List.of()));
  }

  @Override
  public List<Description> visit(ConstructorDeclaration n, Analyzer arg) {
    String name = n.getNameAsString();

    return List.of(
        new ConstructorDescription(new MemberDescription(name), parameters(n, arg), List.of()));
  }

  @Override
  public List<Description> visit(Parameter n, Analyzer arg) {
    return List.of(new ParameterDescription(resolve(n.getType()), n.getNameAsString()));
  }
}
