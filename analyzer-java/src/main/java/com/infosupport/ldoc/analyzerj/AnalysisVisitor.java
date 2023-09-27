package com.infosupport.ldoc.analyzerj;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.infosupport.ldoc.analyzerj.descriptions.AttributeArgumentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.AttributeDescription;
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

  private <T extends Node> List<Description> attributes(NodeWithAnnotations<T> n, Analyzer arg) {
    List<Description> attributes = new ArrayList<>();
    for (AnnotationExpr annotation : n.getAnnotations()) {
      attributes.addAll(annotation.accept(this, arg));
    }
    return attributes;
  }

  @Override
  public List<Description> visit(ClassOrInterfaceDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = new ArrayList<>();
    baseTypes.addAll(resolve(n.getExtendedTypes()));
    baseTypes.addAll(resolve(n.getImplementedTypes()));

    return List.of(new TypeDescription(
        n.isInterface() ? TypeType.INTERFACE : TypeType.CLASS, fullName, baseTypes,
        ctors(n, arg), methods(n, arg), attributes(n, arg)));
  }

  @Override
  public List<Description> visit(RecordDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = resolve(n.getImplementedTypes());

    return List.of(new TypeDescription(
        TypeType.STRUCT, fullName, baseTypes,
        ctors(n, arg), methods(n, arg), attributes(n, arg)));
  }

  @Override
  public List<Description> visit(EnumDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = resolve(n.getImplementedTypes());

    return List.of(new TypeDescription(
        TypeType.ENUM, fullName, baseTypes,
        ctors(n, arg), methods(n, arg), attributes(n, arg)));
  }

  @Override
  public List<Description> visit(MethodDeclaration n, Analyzer arg) {
    String name = n.getNameAsString();
    String retType = resolve(n.getType());

    return List.of(new MethodDescription(
        new MemberDescription(name, attributes(n, arg)),
        retType, parameters(n, arg), List.of()));
  }

  @Override
  public List<Description> visit(ConstructorDeclaration n, Analyzer arg) {
    String name = n.getNameAsString();

    return List.of(new ConstructorDescription(
        new MemberDescription(name, attributes(n, arg)),
        parameters(n, arg), List.of()));
  }

  @Override
  public List<Description> visit(Parameter n, Analyzer arg) {
    return List.of(
        new ParameterDescription(resolve(n.getType()), n.getNameAsString(), attributes(n, arg)));
  }

  private List<Description> visitAnnotation(AnnotationExpr n, List<Description> args) {
    var type = resolver.resolveDeclaration(n, ResolvedAnnotationDeclaration.class);

    return List.of(new AttributeDescription(type.getQualifiedName(), n.getNameAsString(), args));
  }

  @Override
  public List<Description> visit(MarkerAnnotationExpr n, Analyzer arg) {
    return visitAnnotation(n, List.of());
  }

  @Override
  public List<Description> visit(SingleMemberAnnotationExpr n, Analyzer arg) {
    List<Description> args = List.of(new AttributeArgumentDescription(
        "value",
        n.getMemberValue().calculateResolvedType().describe(),
        n.getMemberValue().toString()));
    return visitAnnotation(n, args);
  }

  @Override
  public List<Description> visit(NormalAnnotationExpr n, Analyzer arg) {
    List<Description> args = super.visit(n, arg);
    return visitAnnotation(n, args);
  }

  @Override
  public List<Description> visit(MemberValuePair n, Analyzer arg) {
    return List.of(new AttributeArgumentDescription(
        n.getNameAsString(),
        n.getValue().calculateResolvedType().describe(),
        n.getValue().toString()));
  }
}
