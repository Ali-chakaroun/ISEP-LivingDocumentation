package com.infosupport.ldoc.analyzerj;

import static org.junit.jupiter.api.Assertions.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.TypeDescription;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnalysisVisitorTest {

  private final JavaParser parser;

  AnalysisVisitorTest() {
    parser = new JavaParser(
        new ParserConfiguration().setSymbolResolver(
            new JavaSymbolSolver(
                new ReflectionTypeSolver())));
  }

  private List<Description> parse(String code) {
    return parser.parse(code).getResult().orElseThrow().accept(new AnalysisVisitor(), null);
  }

  @Test
  void type_description_for_class() {
    assertIterableEquals(
        List.of(new TypeDescription("Foo", List.of())),
        parse("class Foo {}"));

    assertIterableEquals(
        List.of(new TypeDescription("some.example.Bar", List.of())),
        parse("package some.example; class Bar {}"));

    assertIterableEquals(
        List.of(new TypeDescription("Baz", List.of("java.lang.Object"))),
        parse("class Baz extends Object {}"));

    assertIterableEquals(
        List.of(new TypeDescription("Baz", List.of("java.io.Serializable"))),
        parse("import java.io.Serializable; class Baz implements Serializable {}"));
  }

  @Test
  void type_description_for_interface() {
    assertIterableEquals(
        List.of(new TypeDescription("Oogle", List.of())),
        parse("interface Oogle {}"));

    assertIterableEquals(
        List.of(new TypeDescription("some.example.Foogle", List.of())),
        parse("package some.example; interface Foogle {}"));

    assertIterableEquals(
        List.of(new TypeDescription("Boogle", List.of("java.lang.Comparable"))),
        parse("interface Boogle extends Comparable {}"));
  }

}
