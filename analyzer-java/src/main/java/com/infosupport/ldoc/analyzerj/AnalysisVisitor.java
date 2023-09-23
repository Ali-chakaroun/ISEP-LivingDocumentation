package com.infosupport.ldoc.analyzerj;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
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
import java.util.function.Predicate;

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

  /** Like {@link #visit} but only returning descriptions for Nodes matching the given predicate. */
  private <T extends Node> List<Description> select(List<T> nodes, Predicate<T> p, Analyzer arg) {
    return nodes.stream().filter(p).flatMap(n -> n.accept(this, arg).stream()).toList();
  }

  @Override
  public List<Description> visit(ClassOrInterfaceDeclaration n, Analyzer arg) {
    List<String> baseTypes = new ArrayList<>();
    baseTypes.addAll(resolve(n.getExtendedTypes()));
    baseTypes.addAll(resolve(n.getImplementedTypes()));

    return List.of(new TypeDescription(
        n.isInterface() ? TypeType.INTERFACE : TypeType.CLASS,
        n.getFullyQualifiedName().orElseThrow(),
        baseTypes,
        select(n.getMembers(), BodyDeclaration::isConstructorDeclaration, arg),
        select(n.getMembers(), BodyDeclaration::isMethodDeclaration, arg),
        visit(n.getAnnotations(), arg)));
  }

  @Override
  public List<Description> visit(RecordDeclaration n, Analyzer arg) {
    return List.of(new TypeDescription(
        TypeType.STRUCT,
        n.getFullyQualifiedName().orElseThrow(),
        resolve(n.getImplementedTypes()),
        select(n.getMembers(), BodyDeclaration::isConstructorDeclaration, arg),
        select(n.getMembers(), BodyDeclaration::isMethodDeclaration, arg),
        visit(n.getAnnotations(), arg)));
  }

  @Override
  public List<Description> visit(EnumDeclaration n, Analyzer arg) {
    return List.of(new TypeDescription(
        TypeType.ENUM,
        n.getFullyQualifiedName().orElseThrow(),
        resolve(n.getImplementedTypes()),
        select(n.getMembers(), BodyDeclaration::isConstructorDeclaration, arg),
        select(n.getMembers(), BodyDeclaration::isMethodDeclaration, arg),
        visit(n.getAnnotations(), arg)));
  }

  @Override
  public List<Description> visit(MethodDeclaration n, Analyzer arg) {
    return List.of(new MethodDescription(
        new MemberDescription(n.getNameAsString(), visit(n.getAnnotations(), arg)),
        resolve(n.getType()),
        visit(n.getParameters(), arg),
        List.of()));
  }

  @Override
  public List<Description> visit(ConstructorDeclaration n, Analyzer arg) {
    return List.of(new ConstructorDescription(
        new MemberDescription(n.getNameAsString(), visit(n.getAnnotations(), arg)),
        visit(n.getParameters(), arg),
        List.of()));
  }

  @Override
  public List<Description> visit(Parameter n, Analyzer arg) {
    return List.of(new ParameterDescription(
        resolve(n.getType()),
        n.getNameAsString(),
        visit(n.getAnnotations(), arg)));
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
