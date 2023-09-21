package com.infosupport.ldoc.analyzerj;

import static org.junit.jupiter.api.Assertions.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.infosupport.ldoc.analyzerj.descriptions.ConstructorDescription;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.MemberDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ParameterDescription;
import com.infosupport.ldoc.analyzerj.descriptions.TypeDescription;
import com.infosupport.ldoc.analyzerj.descriptions.TypeType;
import java.util.List;
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
        List.of(new TypeDescription(TypeType.CLASS, "Bongo", List.of(), List.of(
            new ConstructorDescription(
                new MemberDescription("Bongo"),
                List.of(new ParameterDescription("java.lang.Object", "z")),
                List.of())
        ), List.of())),
        parse("class Bongo { Bongo(Object z) {} }")
    );
  }

  @Test
  void method_description() {
    assertIterableEquals(
        List.of(new TypeDescription(TypeType.CLASS, "Example", List.of(), List.of(), List.of(
          new MethodDescription(
              new MemberDescription("does"),
              "Example",
              List.of(
                  new ParameterDescription("java.lang.Object", "a"),
                  new ParameterDescription("java.lang.String", "b")),
              List.of())))),
        parse("class Example { Example does(Object a, String b) {} }"));
  }
}
