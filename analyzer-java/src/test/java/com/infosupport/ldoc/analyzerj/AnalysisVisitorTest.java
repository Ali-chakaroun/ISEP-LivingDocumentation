package com.infosupport.ldoc.analyzerj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.infosupport.ldoc.analyzerj.descriptions.ArgumentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.AssignmentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.AttributeArgumentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.AttributeDescription;
import com.infosupport.ldoc.analyzerj.descriptions.CommentSummaryDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ConstructorDescription;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
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
        List.of(new TypeDescription(TypeType.CLASS, "Foo", List.of())), parse("class Foo {}"));

    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "some.example.Bar", List.of())),
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
        List.of(new TypeDescription(TypeType.INTERFACE, "Oogle", List.of())),
        parse("interface Oogle {}"));

    assertIterableEquals(
        List.of(new TypeDescription(TypeType.INTERFACE, "some.example.Foogle", List.of())),
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
        List.of(
            new TypeDescription.Builder(TypeType.CLASS, "Bongo")
                .withMembers(
                    new ConstructorDescription(
                        new MemberDescription("Bongo"),
                        List.of(new ParameterDescription("java.lang.Object", "z", List.of())),
                        List.of()))
                .build()),
        parse("class Bongo { Bongo(Object z) {} }"));
  }

  @Test
  void method_description() {
    assertIterableEquals(
        List.of(
            new TypeDescription.Builder(TypeType.CLASS, "Zap")
                .withMembers(
                    new MethodDescription(
                        new MemberDescription("does"),
                        "Zap",
                        List.of(
                            new ParameterDescription("java.lang.Object", "a", List.of()),
                            new ParameterDescription("java.lang.String", "b", List.of())),
                        List.of()))
                .build()),
        parse("class Zap { Zap does(Object a, String b) {} }"));

    assertIterableEquals(
        List.of(
            new TypeDescription.Builder(TypeType.INTERFACE, "Ala")
                .withMembers(
                    new MethodDescription(
                        new MemberDescription("kazam"),
                        null,
                        List.of(),
                        List.of()))
                .build()),
        parse("interface Ala { void kazam(); }"));
  }

  @Test
  void attribute_description() {
    assertIterableEquals(
        List.of(
            new TypeDescription.Builder(TypeType.CLASS, "Z")
                .withAttributes(
                    List.of(
                        new AttributeDescription("java.lang.Deprecated", "Deprecated", List.of())))
                .build()),
        parse("@Deprecated class Z {}"));

    assertIterableEquals(
        List.of(
            new TypeDescription.Builder(TypeType.CLASS, "X")
                .withAttributes(
                    List.of(
                        new AttributeDescription(
                            "java.lang.SuppressWarnings",
                            "SuppressWarnings",
                            List.of(
                                new AttributeArgumentDescription(
                                    "value", "java.lang.String", "\"unchecked\"")))))
                .build()),
        parse("@SuppressWarnings(\"unchecked\") class X {}"));
  }

  @Test
  void return_statement() {
    assertIterableEquals(List.of(new ReturnDescription("")), parseFragment("return;"));

    assertIterableEquals(List.of(new ReturnDescription("1 + 2")), parseFragment("return 1 + 2;"));
  }

  @Test
  void if_statement() {
    assertIterableEquals(
        List.of(
            new IfDescription(
                List.of(new IfElseSection("true", List.of(new ReturnDescription("1")))))),
        parseFragment("if (true) return 1;"));

    assertIterableEquals(
        List.of(
            new IfDescription(
                List.of(
                    new IfElseSection("true", List.of(new ReturnDescription("1"))),
                    new IfElseSection(null, List.of(new ReturnDescription("2")))))),
        parseFragment("if (true) return 1; else return 2;"));

    assertIterableEquals(
        List.of(
            new IfDescription(
                List.of(
                    new IfElseSection("true", List.of(new ReturnDescription("1"))),
                    new IfElseSection("false", List.of(new ReturnDescription("2")))))),
        parseFragment("if (true) return 1; else if (false) return 2;"));

    assertIterableEquals(
        List.of(
            new IfDescription(
                List.of(
                    new IfElseSection("true", List.of(new ReturnDescription("1"))),
                    new IfElseSection("false", List.of(new ReturnDescription("2"))),
                    new IfElseSection(null, List.of(new ReturnDescription("4")))))),
        parseFragment("if (true) return 1; else if (false) return 2; else return 4;"));

    assertIterableEquals(
        List.of(
            new IfDescription(
                List.of(
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
    String fragment =
        """
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
        List.of(
            new SwitchDescription(
                "bongo",
                List.of(
                    new SwitchSection(List.of("1"), List.of(new ReturnDescription("2"))),
                    new SwitchSection(List.of("3"), List.of()),
                    new SwitchSection(List.of("4"), List.of(new ReturnDescription("5"))),
                    new SwitchSection(List.of("default"), List.of(new ReturnDescription("6")))))),
        parseFragment(fragment));
  }

  @Test
  void assign_expr() {
    assertIterableEquals(
        List.of(new AssignmentDescription("john", "=", "paul")), parseFragment("john = paul;"));
  }

  @Test
  void method_call_expr() {
    assertIterableEquals(
        List.of(
            new InvocationDescription(
                "java.io.PrintStream",
                "println",
                List.of(new ArgumentDescription("java.lang.String", "\"Hello!\"")))),
        parseFragment("System.out.println(\"Hello!\");"));
  }

  @Test
  void catch_clause_parameters_ignored() {
    // Regression test for issue #34. The "Exception e" should not be regarded as a parameter.
    assertIterableEquals(
        List.of(new ReturnDescription("1")),
        parseFragment("try { } catch (Exception e) { return 1; }"));
  }

  @Test
  void comment_tests() {
    assertIterableEquals(
        List.of(
            new TypeDescription.Builder(TypeType.CLASS, "Example")
                .withMembers(
                    new MethodDescription(
                        new MemberDescription("does", 0, List.of(),
                            new CommentSummaryDescription(
                                "These are the remarks.",
                                "an Example.",
                                "This method is an example.main<>.",
                                Map.of(
                                    "a", "is an object.",
                                    "b", "is a string.",
                                    "Map<input>", "map of strings."),
                                Map.of("L", "is a list.", "L<C>", "list of characters."))),
                        "Example",
                        List.of(
                            new ParameterDescription("java.lang.Object", "a", List.of()),
                            new ParameterDescription("java.lang.String", "b", List.of())),
                        List.of()))
                .build()),
        parse(
            """
                class Example {
                  /**
                   * This method is an example.main<>.
                   * These are the remarks.
                   * @param a is an object.
                   * @param b is a string.
                   * @param Map<input> map of strings.
                   * @param <L> is a list.
                   * @param <L<C>> list of characters.
                   * @return an Example.
                   */
                  Example does(Object a, String b) {}
                }
                """));
  }

  @Test
  void class_and_method_modifiers() {
    List<Description> parsed =
        parse(
            """
                public final class Sandwich {
                  private strictfp float consume() {}
                  public static Sandwich prepare() {}
                }
                """);

    var type = (TypeDescription) parsed.get(0);
    assertEquals(Modifier.PUBLIC.mask() | Modifier.SEALED.mask(), type.modifiers());

    var consume = (MethodDescription) type.methods().get(0);
    assertEquals(Modifier.PRIVATE.mask(), consume.member().modifiers());

    var prepare = (MethodDescription) type.methods().get(1);
    assertEquals(Modifier.PUBLIC.mask() | Modifier.STATIC.mask(), prepare.member().modifiers());
  }

  @Test
  void field_members_in_classes() {
    List<Description> parsed =
        parse(
            """
                class Person {
                    private String name = "Hai";
                    /** Test javadoc */
                    public int age, age2 = 18;
                    public Person(String name, int age) {
                      this.name = name;
                      this.age = age;
                    }
                    public boolean isAdult() {
                      return this.age > 18;
                    }
                }
                """);

    TypeDescription description = (TypeDescription) parsed.get(0);
    List<FieldDescription> fieldDescriptions =
        description.fields().stream().map(d -> (FieldDescription) d).toList();

    FieldDescription nameField =
        new FieldDescription(
            new MemberDescription("name", Modifier.PRIVATE.mask(), List.of(), null),
            "java.lang.String",
            "\"Hai\"");

    CommentSummaryDescription comment =
        new CommentSummaryDescription(null, null, "Test javadoc", null, null);

    FieldDescription age1Field =
        new FieldDescription(
            new MemberDescription("age", Modifier.PUBLIC.mask(), List.of(), comment), "int", null);

    FieldDescription age2Field =
        new FieldDescription(
            new MemberDescription("age2", Modifier.PUBLIC.mask(), List.of(), comment), "int", "18");
    List<FieldDescription> expectedFields = List.of(nameField, age1Field, age2Field);

    assertIterableEquals(expectedFields, fieldDescriptions);
  }

  @Test
  void enum_members() {
    assertIterableEquals(
        List.of(
            new TypeDescription.Builder(TypeType.ENUM, "Nationality")
                .withMembers(
                    new EnumMemberDescription(
                        new MemberDescription("DUTCH", Modifier.PUBLIC.mask(), List.of(), null),
                        List.of()),
                    new EnumMemberDescription(
                        new MemberDescription("GERMAN", Modifier.PUBLIC.mask(), List.of(), null),
                        List.of()))
                .build()),
        parse("enum Nationality { DUTCH, GERMAN; }"));
  }

  @Test
  void enum_members_with_arguments() {
    List<Description> parsed =
        parse(
            """
                  enum Nationality {
                         DUTCH(5),
                         GERMAN(10);

                         int counter;

                         Nationality(int val) {
                           this.counter=val;
                         }
                       }
                """);

    TypeDescription typeDescription = (TypeDescription) parsed.get(0);

    TypeDescription expected =
        new TypeDescription.Builder(TypeType.ENUM, "Nationality")
            .withMembers(
                new FieldDescription(
                    new MemberDescription("counter", Modifier.NONE.mask(), List.of(), null),
                    "int",
                    null),
                new ConstructorDescription(
                    new MemberDescription("Nationality"),
                    List.of(new ParameterDescription("int", "val", List.of())),
                    List.of(new AssignmentDescription("this.counter", "=", "val"))),
                new EnumMemberDescription(
                    new MemberDescription("DUTCH", Modifier.PUBLIC.mask(), List.of(), null),
                    List.of(new ArgumentDescription("int", "5"))),
                new EnumMemberDescription(
                    new MemberDescription("GERMAN", Modifier.PUBLIC.mask(), List.of(), null),
                    List.of(new ArgumentDescription("int", "10"))))
            .build();

    assertEquals(expected, typeDescription);
  }
}
