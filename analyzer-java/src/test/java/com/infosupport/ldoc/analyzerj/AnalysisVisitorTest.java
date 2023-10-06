package com.infosupport.ldoc.analyzerj;

import static org.junit.jupiter.api.Assertions.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.infosupport.ldoc.analyzerj.descriptions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class AnalysisVisitorTest {

  private final JavaParser parser;
  private final SymbolResolver solver;

  AnalysisVisitorTest() {
    solver = new JavaSymbolSolver(new ReflectionTypeSolver());
    parser = new JavaParser(new ParserConfiguration().setSymbolResolver(solver));
  }

  private List<Description> parse(String code) {
    return parser.parse(code).getResult().orElseThrow().accept(new AnalysisVisitor(solver), null);
  }

  private List<Description> parseFragment(String fragment) {
    String source = String.format("class Test { int test() { %s } }", fragment);
    List<Description> unit = parse(source);
    List<Description> methods = ((TypeDescription) unit.get(0)).methods();
    List<Description> statements = ((MethodDescription) methods.get(0)).statements();
    assertNotEquals(statements, List.of());
    return statements;
  }

  @Test
  void type_description_for_class() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "Foo")),
        parse("class Foo {}"));

    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "some.example.Bar")),
        parse("package some.example; class Bar {}"));

    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "Baz", List.of("java.lang.Object"))),
        parse("class Baz extends Object {}"));

    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "Fum", List.of("java.io.Serializable"))),
        parse("import java.io.Serializable; class Fum implements Serializable {}"));
  }

  @Test
  void type_description_for_interface() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.INTERFACE, "Oogle")),
        parse("interface Oogle {}"));

    assertIterableEquals(
        List.of(new TypeDescription(TypeType.INTERFACE, "some.example.Foogle")),
        parse("package some.example; interface Foogle {}"));

    assertIterableEquals(
        List.of(new TypeDescription(TypeType.INTERFACE, "Boogle", List.of("java.lang.Comparable"))),
        parse("interface Boogle extends Comparable {}"));
  }

  @Test
  void type_description_for_record() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.STRUCT, "Blarg", List.of("java.lang.Runnable"))),
        parse("record Blarg() implements Runnable {}"));
  }

  @Test
  void type_description_for_enum() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.ENUM, "Pippo", List.of("java.lang.Cloneable"))),
        parse("enum Pippo implements Cloneable {}"));
  }

  @Test
  void constructor_description() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "Bongo", List.of(), null, List.of(
            new ConstructorDescription(
                new MemberDescription("Bongo"),
                List.of(new ParameterDescription("java.lang.Object", "z", List.of())),
                List.of())
        ), List.of(), List.of())),
        parse("class Bongo { Bongo(Object z) {} }")
    );
  }

  @Test
  void method_description() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "Example", List.of(), null, List.of(), List.of(
            new MethodDescription(
                new MemberDescription("does"),
                "Example",
                null,
                List.of(
                    new ParameterDescription("java.lang.Object", "a", List.of()),
                    new ParameterDescription("java.lang.String", "b", List.of())),
                List.of())), List.of())),
        parse("class Example { Example does(Object a, String b) {} }"));
  }

  @Test
  void attribute_description() {
    assertIterableEquals(
        List.of(
            new TypeDescription(TypeType.CLASS, "Z", List.of(), null, List.of(), List.of(), List.of(
                new AttributeDescription("java.lang.Deprecated", "Deprecated", List.of())))),
        parse("@Deprecated class Z {}"));

    assertIterableEquals(
        List.of(
            new TypeDescription(TypeType.CLASS, "X", List.of(), null, List.of(), List.of(), List.of(
                new AttributeDescription("java.lang.SuppressWarnings", "SuppressWarnings", List.of(
                    new AttributeArgumentDescription("value", "java.lang.String",
                        "\"unchecked\"")))))),
        parse("@SuppressWarnings(\"unchecked\") class X {}"));
  }

  @Test
  void return_statement() {
    assertIterableEquals(
        List.of(new ReturnDescription()),
        parseFragment("return;"));

    assertIterableEquals(
        List.of(new ReturnDescription("1 + 2")),
        parseFragment("return 1 + 2;"));
  }

  @Test
  void if_statement() {
    assertIterableEquals(
        List.of(new IfDescription(List.of(
            new IfElseSection("true", List.of(new ReturnDescription("1")))))),
        parseFragment("if (true) return 1;"));

    assertIterableEquals(
        List.of(new IfDescription(List.of(
            new IfElseSection("true", List.of(new ReturnDescription("1"))),
            new IfElseSection(null, List.of(new ReturnDescription("2")))))),
        parseFragment("if (true) return 1; else return 2;"));

    assertIterableEquals(
        List.of(new IfDescription(List.of(
            new IfElseSection("true", List.of(new ReturnDescription("1"))),
            new IfElseSection("false", List.of(new ReturnDescription("2")))))),
        parseFragment("if (true) return 1; else if (false) return 2;"));

    assertIterableEquals(
        List.of(new IfDescription(List.of(
            new IfElseSection("true", List.of(new ReturnDescription("1"))),
            new IfElseSection("false", List.of(new ReturnDescription("2"))),
            new IfElseSection(null, List.of(new ReturnDescription("4")))))),
        parseFragment("if (true) return 1; else if (false) return 2; else return 4;"));

    assertIterableEquals(
        List.of(new IfDescription(List.of(
            new IfElseSection("true", List.of(new ReturnDescription("1"))),
            new IfElseSection("false", List.of(new ReturnDescription("2"))),
            new IfElseSection(null, List.of(new ReturnDescription("4")))))),
        parseFragment("if (true) return 1; else if (false) return 2; else return 4;"));
  }

  @Test
  void foreach_statement() {
    assertIterableEquals(
        List.of(new ForEachDescription("Object x : xs", List.of(new ReturnDescription("1")))),
        parseFragment("for (Object x : xs) { return 1; }"));
  }

  @Test
  void switch_statement() {
    String fragment = """
        switch (bongo) {
          case 1:
            return 2;
          case 3:
          case 4:
            return 5;
          default:
            return 6;
        }
        """;
    assertIterableEquals(
        List.of(new SwitchDescription("bongo", List.of(
            new SwitchSection(List.of("1"), List.of(new ReturnDescription("2"))),
            new SwitchSection(List.of("3"), List.of()),
            new SwitchSection(List.of("4"), List.of(new ReturnDescription("5"))),
            new SwitchSection(List.of("default"), List.of(new ReturnDescription("6")))))),
        parseFragment(fragment));
  }

  @Test
  void assign_expr() {
    assertIterableEquals(
        List.of(new AssignmentDescription("john", "=", "paul")),
        parseFragment("john = paul;"));
  }

  @Test
  void method_call_expr() {
    assertIterableEquals(
        List.of(new InvocationDescription("java.io.PrintStream", "println", List.of(
            new ArgumentDescription("java.lang.String", "\"Hello!\"")))),
        parseFragment("System.out.println(\"Hello!\");"));
  }

  @Test
  void comment_tests() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "Example", List.of(), null, List.of(), List.of(
            new MethodDescription(
                new MemberDescription("does"),
                "Example",
                new CommentSummaryDescription("These are the remarks.",
                    "an Example.", "This method is an example.",
                    Map.of("a", "is an object.", "b", "is a string."), null),
                List.of(
                    new ParameterDescription("java.lang.Object", "a", List.of()),
                    new ParameterDescription("java.lang.String", "b", List.of())),
                List.of())), List.of())),
        parse("""
            class Example {
              /**
               * This method is an example. These are the remarks.
               * @param a is an object.
               * @param b is a string.
               * @return an Example.
               */
              Example does(Object a, String b) {}
            }
            """));
  }
}
