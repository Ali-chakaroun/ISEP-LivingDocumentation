package com.infosupport.ldoc.analyzerj;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
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
import com.infosupport.ldoc.analyzerj.descriptions.ArgumentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.AssignmentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.AttributeArgumentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.AttributeDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ConstructorDescription;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.DocumentationCommentsDescription;
import com.infosupport.ldoc.analyzerj.descriptions.EnumMemberDescription;
import com.infosupport.ldoc.analyzerj.descriptions.FieldDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ForEachDescription;
import com.infosupport.ldoc.analyzerj.descriptions.IfDescription;
import com.infosupport.ldoc.analyzerj.descriptions.IfElseSection;
import com.infosupport.ldoc.analyzerj.descriptions.InvocationDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MemberDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import com.infosupport.ldoc.analyzerj.descriptions.Modifier;
import com.infosupport.ldoc.analyzerj.descriptions.ParameterDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ReturnDescription;
import com.infosupport.ldoc.analyzerj.descriptions.SwitchDescription;
import com.infosupport.ldoc.analyzerj.descriptions.SwitchSection;
import com.infosupport.ldoc.analyzerj.descriptions.TypeDescription;
import com.infosupport.ldoc.analyzerj.descriptions.TypeType;
import com.infosupport.ldoc.analyzerj.helpermethods.CommentHelperMethods;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AnalysisVisitor converts a JavaParser parse tree into a list of {@link Description} objects. The
 * resulting Descriptions may in turn have lists of children, forming a tree. Only the subset of the
 * AST that is relevant for LivingDocumentation is described. Types are resolved to their fully
 * qualified names.
 */
public class AnalysisVisitor extends GenericListVisitorAdapter<Description, Analyzer> {

  private final SymbolResolver resolver;

  /** Construct an AnalysisVisitor that uses the given SymbolResolver to find the names of types. */
  public AnalysisVisitor(SymbolResolver resolver) {
    this.resolver = resolver;
  }

  /**
   * Resolves the given Type to a String with its fully-qualified class name.
   * */
  private String resolve(Type t) {
    try {
      return resolver.toResolvedType(t, ResolvedType.class).describe();
    } catch (UnsupportedOperationException | IllegalArgumentException e) {
      // References to records can not be resolved yet (issue #66);
      // Apparently, in case the to be resolved Record lies in a separate compilation unit,
      //    an illegal argument exception is thrown.
      return "?";
    }
  }

  /**
   * Resolves the given AnnotationExpression to a String with its fully-qualified class name.
   * */
  private String resolve(AnnotationExpr ae) {
    try {
      return resolver.resolveDeclaration(ae, ResolvedAnnotationDeclaration.class)
          .getQualifiedName();
    } catch (UnsupportedOperationException | IllegalArgumentException e) {
      // References to records can not be resolved yet (issue #66);
      // Apparently, in case the to be resolved Record lies in a separate compilation unit,
      //    an illegal argument exception is thrown.
      return "?";
    }
  }

  /**
   * Resolves the given Expression to a String with its fully-qualified class name.
   * */
  private String resolve(Expression e) {
    try {
      return resolver.calculateType(e).describe();
    } catch (UnsupportedOperationException | IllegalArgumentException exception) {
      // References to records can not be resolved yet (issue #66);
      // Apparently, in case the to be resolved Record lies in a separate compilation unit,
      //    an illegal argument exception is thrown.
      return "?";
    }
  }

  /** Resolves all the given class or interface types to their fully-qualified names, as String. */
  private List<String> resolve(List<ClassOrInterfaceType> types) {
    return types.stream().map(this::resolve).toList();
  }

  /** Computes an OR-combined LivingDocumentation bitmask for a NodeList of JavaParser Modifiers. */
  private int combine(NodeList<com.github.javaparser.ast.Modifier> modifiers) {
    return modifiers.stream().mapToInt(m -> Modifier.valueOf(m).mask()).reduce(0, (a, b) -> a | b);
  }

  /** Describes an annotation (Java) as an Attribute (LivingDocumentation) with given arguments. */
  private List<Description> visitAnnotation(AnnotationExpr n, List<Description> args) {
    String type = resolve(n);
    return List.of(new AttributeDescription(type, n.getNameAsString(), args));
  }

  private TypeDescription.Builder typeBuilder(TypeType typeType,
      TypeDeclaration<? extends TypeDeclaration<?>> n, Analyzer arg) {
    String name = n.getFullyQualifiedName().orElseThrow();
    Description comment = n.getComment()
        .flatMap(z -> z.accept(this, arg).stream().findFirst())
        .orElse(null);
    return new TypeDescription.Builder(typeType, name)
        .withModifiers(combine(n.getModifiers()))
        .withComment(comment)
        .withMembers(visit(n.getMembers(), arg))
        .withAttributes(visit(n.getAnnotations(), arg));
  }

  /** Describe a top-level compilation unit. */
  @Override
  public List<Description> visit(CompilationUnit n, Analyzer arg) {
    // We do not care about top-level comments, e.g. those in package-info.java. Remove them.
    n.removeComment();
    // Other than that, the default behavior is fine.
    return super.visit(n, arg);
  }

  /** Describes a class or interface (Java) as a Type with TypeType CLASS or INTERFACE. */
  @Override
  public List<Description> visit(ClassOrInterfaceDeclaration n, Analyzer arg) {
    TypeType typeType = n.isInterface() ? TypeType.INTERFACE : TypeType.CLASS;
    List<String> baseTypes = new ArrayList<>();
    baseTypes.addAll(resolve(n.getExtendedTypes()));
    baseTypes.addAll(resolve(n.getImplementedTypes()));

    return List.of(typeBuilder(typeType, n, arg).withBaseTypes(baseTypes).build());
  }

  /** Describes a record class (Java) as a Type with TypeType STRUCT. */
  @Override
  public List<Description> visit(RecordDeclaration n, Analyzer arg) {
    return List.of(
        typeBuilder(TypeType.STRUCT, n, arg)
            .withBaseTypes(resolve(n.getImplementedTypes()))
            .build());
  }

  /** Describes an enum type (Java) as a Type with TypeType ENUM. */
  @Override
  public List<Description> visit(EnumDeclaration n, Analyzer arg) {
    return List.of(
        typeBuilder(TypeType.ENUM, n, arg)
            .withBaseTypes(resolve(n.getImplementedTypes()))
            .withMembers(visit(n.getEntries(), arg))
            .build());
  }

  /** Describes an annotation declaration (or annotation interface; Java)
   *    as a TypeType INTERFACE. */
  @Override
  public List<Description> visit(AnnotationDeclaration n, Analyzer arg) {
    return List.of(
        typeBuilder(TypeType.INTERFACE, n, arg).build()
    );
  }

  /**
   * Maps a Java AnnotationMemeberDeclaration to a FieldDescription.
   *
   * @param n   node of type AnnotationMemberDeclaration
   * @param arg Analyzer to be used
   * @return FieldDescription with the name of method as member name, return type as type,
   *      and default value as initialValue
   */
  @Override
  public List<Description> visit(AnnotationMemberDeclaration n, Analyzer arg) {
    return List.of(
        new FieldDescription(
            new MemberDescription(n.getNameAsString(), Modifier.NONE.mask(),
                visit(n.getAnnotations(), arg), n.getComment()
                    .flatMap(c -> c.accept(this, arg).stream().findFirst()).orElse(null)),
                resolve(n.getType()),
                n.getDefaultValue().map(Object::toString).orElse(null)
        )
    );
  }

  /**
   * Create a list of EnumMemberDescriptions from the JavaParser EnumConstantDeclaration. Sets the
   * member description modifiers to Public as Java does not have modifiers for Enum literals but
   * are public by design.
   *
   * @param n   node of type EnumConstantDeclaration
   * @param arg Analyzer to be used
   * @return List of EnumMemberDescriptions.
   */
  @Override
  public List<Description> visit(EnumConstantDeclaration n, Analyzer arg) {
    List<Description> arguments = new ArrayList<>();

    for (Expression argument : n.getArguments()) {
      String type = resolve(argument);
      String value = argument.toString();
      arguments.add(new ArgumentDescription(type, value));
    }

    return List.of(
        new EnumMemberDescription(
            new MemberDescription(
                n.getNameAsString(), Modifier.PUBLIC.mask(), visit(n.getAnnotations(), arg),
                n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst())
                    .orElse(null)),
            arguments));
  }

  /**
   * Generates a list of field descriptions Note that a FieldDeclaration may contain multiple
   * variables (i.e., int a, b = 10; or even int a = 10, b = 5;) These will be split as separate
   * fields, note that the comment will be duplicated in this case.
   *
   * @param n   node of type FieldDeclaration
   * @param arg Analyzer to be used
   * @return List of FieldDescriptions, one for each variable in the field.
   */
  @Override
  public List<Description> visit(FieldDeclaration n, Analyzer arg) {
    List<Description> fieldDescriptions = new ArrayList<>();

    for (VariableDeclarator variable : n.getVariables()) {
      // Get the initializer as a literal String (i.e., without quotation marks)
      //    when null, will be ignored by the JsonInclude
      String initializer = variable.getInitializer().map(Object::toString).orElse(null);

      fieldDescriptions.add(
          new FieldDescription(
              new MemberDescription(
                  variable.getNameAsString(),
                  combine(n.getModifiers()),
                  visit(n.getAnnotations(), arg),
                  n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst())
                      .orElse(null)),
              resolve(variable.getType()),
              initializer));
    }

    return fieldDescriptions;
  }

  /** Describes a method. */
  @Override
  public List<Description> visit(MethodDeclaration n, Analyzer arg) {
    return List.of(
        new MethodDescription(
            new MemberDescription(
                n.getNameAsString(), combine(n.getModifiers()), visit(n.getAnnotations(), arg),
                n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst())
                    .orElse(null)),
            n.getType().isVoidType() ? null : resolve(n.getType()),
            visit(n.getParameters(), arg),
            n.getBody().map(z -> z.accept(this, arg)).orElse(List.of())));
  }

  /** Describes a constructor. */
  @Override
  public List<Description> visit(ConstructorDeclaration n, Analyzer arg) {
    return List.of(
        new ConstructorDescription(
            new MemberDescription(
                n.getNameAsString(), combine(n.getModifiers()), visit(n.getAnnotations(), arg),
                n.getComment().flatMap(c -> c.accept(this, arg).stream().findFirst())
                    .orElse(null)),
            visit(n.getParameters(), arg),
            visit(n.getBody(), arg)));
  }

  /** Describes a method or constructor (formal) parameter. */
  @Override
  public List<Description> visit(Parameter n, Analyzer arg) {
    return List.of(
        new ParameterDescription(
            resolve(n.getType()), n.getNameAsString(), visit(n.getAnnotations(), arg)));
  }

  /** Describes an annotation without arguments, as an {@link AttributeDescription}. */
  @Override
  public List<Description> visit(MarkerAnnotationExpr n, Analyzer arg) {
    return visitAnnotation(n, List.of());
  }

  /**
   * Describes an annotation with one argument, which is always named 'value' by convention, as an
   * {@link AttributeDescription}.
   */
  @Override
  public List<Description> visit(SingleMemberAnnotationExpr n, Analyzer arg) {
    List<Description> args =
        List.of(
            new AttributeArgumentDescription(
                "value",
                resolve(n.getMemberValue()),
                n.getMemberValue().toString()));
    return visitAnnotation(n, args);
  }

  /**
   * Describes an annotation that has one or more member-value pair arguments (so a 'normal'
   * annotation) as an {@link AttributeDescription}.
   */
  @Override
  public List<Description> visit(NormalAnnotationExpr n, Analyzer arg) {
    List<Description> args = super.visit(n, arg);
    return visitAnnotation(n, args);
  }

  /**
   * Describes an annotation argument member-value pair as an {@link AttributeArgumentDescription}.
   */
  @Override
  public List<Description> visit(MemberValuePair n, Analyzer arg) {
    return List.of(
        new AttributeArgumentDescription(
            n.getNameAsString(),
            resolve(n.getValue()),
            n.getValue().toString()));
  }

  /** Describes a <code>return</code> statement, including the returned expression if present. */
  @Override
  public List<Description> visit(ReturnStmt n, Analyzer arg) {
    return List.of(new ReturnDescription(n.getExpression().map(Node::toString).orElse("")));
  }

  /** Describes an <code>if</code> statement or tree of <code>if</code> statements. */
  @Override
  public List<Description> visit(IfStmt n, Analyzer arg) {
    List<Description> sections = new ArrayList<>();
    String condition = n.getCondition().toString();
    List<Description> consequent = n.getThenStmt().accept(this, arg);
    sections.add(new IfElseSection(condition, consequent));

    if (n.hasElseBranch()) {
      List<Description> alternative = n.getElseStmt().orElseThrow().accept(this, arg);

      /* Flatten if-else trees. In LivingDocumentation JSON, a big tree of if-else structures "goes
       * with" the topmost if; instead of each if having one or two branches, we think of it having
       * many branches. */
      if (n.hasCascadingIfStmt() && alternative.get(0) instanceof IfDescription alt) {
        sections.addAll(alt.sections());
      } else {
        sections.add(new IfElseSection(null, alternative));
      }
    }

    return List.of(new IfDescription(sections));
  }

  /** Describes a <code>for</code> for-each loop statement. */
  @Override
  public List<Description> visit(ForEachStmt n, Analyzer arg) {
    String head = String.format("%s : %s", n.getVariable(), n.getIterable());
    return List.of(new ForEachDescription(head, n.getBody().accept(this, arg)));
  }

  /** Describes a <code>switch</code> statement, describing each of the cases in turn. */
  @Override
  public List<Description> visit(SwitchStmt n, Analyzer arg) {
    String head = n.getSelector().toString();
    return List.of(new SwitchDescription(head, n.getEntries().accept(this, arg)));
  }

  /** Describe a single <code>switch</code> entry (JavaParser) or section (LivingDocumentation). */
  @Override
  public List<Description> visit(SwitchEntry n, Analyzer arg) {
    List<String> labels = n.getLabels().stream().map(Node::toString).toList();
    return List.of(
        new SwitchSection(
            labels.equals(List.of()) ? List.of("default") : labels,
            n.getStatements().accept(this, arg)));
  }

  /** Describe a variable assignment. */
  @Override
  public List<Description> visit(AssignExpr n, Analyzer arg) {
    return List.of(
        new AssignmentDescription(n.getTarget().toString(), "=", n.getValue().toString()));
  }

  /** Describe a method call as an {@link InvocationDescription}. */
  @Override
  public List<Description> visit(MethodCallExpr n, Analyzer arg) {

    List<Description> arguments = new ArrayList<>();
    for (Expression argument : n.getArguments()) {
      String type = resolve(argument);
      String text = argument.toString();
      arguments.add(new ArgumentDescription(type, text));
    }
    return List.of(
        new InvocationDescription(
            n.getScope().map(this::resolve).orElse("?"),
            n.getNameAsString(),
            arguments));
  }

  /** Describe catch clause contents, skipping the list of caught exceptions (catch parameters). */
  @Override
  public List<Description> visit(CatchClause n, Analyzer arg) {
    List<Description> out = n.getBody().accept(this, arg);
    return (out != null) ? out : List.of();
  }

  /** Describe a doc comment as a {@link DocumentationCommentsDescription}. */
  @Override
  public List<Description> visit(JavadocComment n, Analyzer arg) {
    StringBuilder returns = new StringBuilder();
    Map<String, String> commentParams = new LinkedHashMap<>();
    Map<String, String> commentTypeParams = new LinkedHashMap<>();
    // Regex: Split the sentence after the first period followed by a whitespace.
    String[] sentences = CommentHelperMethods.extractSummary(n).split("(?<=\\.)\\s", 2);
    String summary =
        (sentences.length > 0 && !sentences[0].isEmpty()) ? sentences[0].strip() : null;
    String remarks = (sentences.length > 1) ? sentences[1].strip() : null;
    Map<String, Map<String, String>> commentData = CommentHelperMethods.extractParamDescriptions(n);
    CommentHelperMethods.processCommentData(commentData, returns, commentParams, commentTypeParams);
    return List.of(
        new DocumentationCommentsDescription(
            remarks,
            !returns.isEmpty() ? returns.toString() : null,
            summary,
            !commentParams.isEmpty() ? commentParams : null,
            !commentTypeParams.isEmpty() ? commentTypeParams : null));
  }

  /**
   * Describes a Variable Declaration Expression. As these are not supported in the JSON, there is
   * no matching description. This method is explicitly implemented to return an empty list in order
   * to prevent annotations being visited that are attached to the Variable Declaration.
   */
  @Override
  public List<Description> visit(VariableDeclarationExpr n, Analyzer arg) {
    return List.of();
  }
}
