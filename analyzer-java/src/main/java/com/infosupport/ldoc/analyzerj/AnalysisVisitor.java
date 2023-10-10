package com.infosupport.ldoc.analyzerj;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.infosupport.ldoc.analyzerj.descriptions.*;
import com.infosupport.ldoc.analyzerj.helperMethods.CommentHelperMethods;

import java.util.*;
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

  /** Computes an OR-combined LivingDocumentation bitmask for a NodeList of JavaParser Modifiers. */
  private int combine(NodeList<Modifier> modifiers) {
    return modifiers.stream().mapToInt(m -> Modifiers.valueOf(m).mask()).reduce(0, (a, b) -> a | b);
  }

  @Override
  public List<Description> visit(ClassOrInterfaceDeclaration n, Analyzer arg) {
    List<String> baseTypes = new ArrayList<>();
    baseTypes.addAll(resolve(n.getExtendedTypes()));
    baseTypes.addAll(resolve(n.getImplementedTypes()));

    return List.of(new TypeDescription(
        n.isInterface() ? TypeType.INTERFACE : TypeType.CLASS,
        combine(n.getModifiers()),
        n.getFullyQualifiedName().orElseThrow(),
        baseTypes,
        n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst()).orElse(null),
        select(n.getMembers(), BodyDeclaration::isConstructorDeclaration, arg),
        select(n.getMembers(), BodyDeclaration::isMethodDeclaration, arg),
        visit(n.getAnnotations(), arg)));
  }

  @Override
  public List<Description> visit(RecordDeclaration n, Analyzer arg) {
    return List.of(new TypeDescription(
        TypeType.STRUCT,
        combine(n.getModifiers()),
        n.getFullyQualifiedName().orElseThrow(),
        resolve(n.getImplementedTypes()),
        n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst()).orElse(null),
        select(n.getMembers(), BodyDeclaration::isConstructorDeclaration, arg),
        select(n.getMembers(), BodyDeclaration::isMethodDeclaration, arg),
        visit(n.getAnnotations(), arg)));
  }

  @Override
  public List<Description> visit(EnumDeclaration n, Analyzer arg) {
    return List.of(new TypeDescription(
        TypeType.ENUM,
        combine(n.getModifiers()),
        n.getFullyQualifiedName().orElseThrow(),
        resolve(n.getImplementedTypes()),
        n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst()).orElse(null),
        select(n.getMembers(), BodyDeclaration::isConstructorDeclaration, arg),
        select(n.getMembers(), BodyDeclaration::isMethodDeclaration, arg),
        visit(n.getAnnotations(), arg)));
  }

  @Override
  public List<Description> visit(MethodDeclaration n, Analyzer arg) {
    return List.of(new MethodDescription(
        new MemberDescription(
            n.getNameAsString(), combine(n.getModifiers()), visit(n.getAnnotations(), arg)),
        resolve(n.getType()),
        n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst()).orElse(null),
        visit(n.getParameters(), arg),
        n.getBody().map(z -> z.accept(this, arg)).orElse(List.of())));
  }

  @Override
  public List<Description> visit(ConstructorDeclaration n, Analyzer arg) {
    return List.of(new ConstructorDescription(
        new MemberDescription(
            n.getNameAsString(), combine(n.getModifiers()), visit(n.getAnnotations(), arg)),
        visit(n.getParameters(), arg), visit(n.getBody(), arg)));
  }

  @Override
  public List<Description> visit(Parameter n, Analyzer arg) {
    return List.of(new ParameterDescription(resolve(n.getType()), n.getNameAsString(),
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
    List<Description> args = List.of(new AttributeArgumentDescription("value",
        resolver.calculateType(n.getMemberValue()).describe(), n.getMemberValue().toString()));
    return visitAnnotation(n, args);
  }

  @Override
  public List<Description> visit(NormalAnnotationExpr n, Analyzer arg) {
    List<Description> args = super.visit(n, arg);
    return visitAnnotation(n, args);
  }

  @Override
  public List<Description> visit(MemberValuePair n, Analyzer arg) {
    return List.of(new AttributeArgumentDescription(n.getNameAsString(),
        resolver.calculateType(n.getValue()).describe(), n.getValue().toString()));
  }

  @Override
  public List<Description> visit(ReturnStmt n, Analyzer arg) {
    return List.of(new ReturnDescription(n.getExpression().map(Node::toString).orElse(null)));
  }

  @Override
  public List<Description> visit(IfStmt n, Analyzer arg) {
    List<Description> sections = new ArrayList<>();
    String condition = n.getCondition().toString();
    List<Description> consequent = n.getThenStmt().accept(this, arg);
    sections.add(new IfElseSection(condition, consequent));

    if (n.hasElseBranch()) {
      List<Description> alternative = n.getElseStmt().orElseThrow().accept(this, arg);

      /* Flatten if-else trees. In LivingDocumentation JSON, a big tree of if-else structures "goes
       with" the topmost if; instead of each if having one or two branches, we think of it having
      many branches. */
      if (n.hasCascadingIfStmt() && alternative.get(0) instanceof IfDescription alt) {
        sections.addAll(alt.sections());
      } else {
        sections.add(new IfElseSection(null, alternative));
      }
    }

    return List.of(new IfDescription(sections));
  }

  @Override
  public List<Description> visit(ForEachStmt n, Analyzer arg) {
    String head = String.format("%s : %s", n.getVariable(), n.getIterable());
    return List.of(new ForEachDescription(head, n.getBody().accept(this, arg)));
  }

  @Override
  public List<Description> visit(SwitchStmt n, Analyzer arg) {
    String head = n.getSelector().toString();
    return List.of(new SwitchDescription(head, n.getEntries().accept(this, arg)));
  }

  @Override
  public List<Description> visit(SwitchEntry n, Analyzer arg) {
    List<String> labels = n.getLabels().stream().map(Node::toString).toList();
    return List.of(new SwitchSection(labels.equals(List.of()) ? List.of("default") : labels,
        n.getStatements().accept(this, arg)));
  }

  @Override
  public List<Description> visit(AssignExpr n, Analyzer arg) {
    return List.of(
        new AssignmentDescription(n.getTarget().toString(), "=", n.getValue().toString()));
  }

  @Override
  public List<Description> visit(MethodCallExpr n, Analyzer arg) {

    List<Description> arguments = new ArrayList<>();
    for (Expression argument : n.getArguments()) {
      String type = resolver.calculateType(argument).describe();
      String text = argument.toString();
      arguments.add(new ArgumentDescription(type, text));
    }
    return List.of(new InvocationDescription(
        n.getScope().map(s -> resolver.calculateType(s).describe()).orElse("?"),
        n.getNameAsString(), arguments));
  }

  @Override
  public List<Description> visit(CatchClause n, Analyzer arg) {
    // Unlike the method we're overriding, we ignore catch clause comments and parameters.
    List<Description> out = n.getBody().accept(this, arg);
    return (out != null) ? out : List.of();
  }

  @Override
  public List<Description> visit(JavadocComment n, Analyzer arg) {
    StringBuilder returns = new StringBuilder();
    Map<String, String> commentParams = new LinkedHashMap<>();
    Map<String, String> commentTypeParams = new LinkedHashMap<>();
    // This regex splits the sentence into 2 if it ends with a . is followed by empty space.
    String[] sentences = CommentHelperMethods.extractSummary(n).split("<remarks>", 2);
    // Give back the . that is removed by the split.
    String summary = (sentences.length > 0) ? sentences[0].strip() : null;
    String remarks = (sentences.length > 1) ? sentences[1].strip() : null;
    Map<String, Map<String, String>> commentData = CommentHelperMethods.extractParamDescriptions(n);
    CommentHelperMethods.processCommentData(commentData, returns, commentParams, commentTypeParams);
    return List.of(
        new CommentSummaryDescription(
            remarks,
            !returns.isEmpty() ? returns.toString() : null,
            summary,
            !commentParams.isEmpty() ? commentParams : null,
            !commentTypeParams.isEmpty() ? commentTypeParams : null));
  }

}
