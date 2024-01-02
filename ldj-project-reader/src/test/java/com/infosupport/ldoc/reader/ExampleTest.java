package com.infosupport.ldoc.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infosupport.ldoc.reader.impl.JacksonProjectFactory;
import com.infosupport.ldoc.reader.visitor.BaseVisitor;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests with example.json, which is based on the Spring event example app.
 */
class ExampleTest {

  private final Project project;

  ExampleTest() throws IOException {
    project = new JacksonProjectFactory().project(ExampleTest.class.getResource("example.json"));
  }

  @Test
  void test_class() {
    Class example = project.classes().findFirst().orElseThrow();

    assertEquals("EventApp", example.name());
    assertEquals("org.example.EventApp", example.fullName());

    assertNull(example.basetypes());
    assertEquals(0, example.constructors().count());
    assertEquals(0, example.enumMembers().count());
    assertEquals(0, example.properties().count());
    assertEquals(0, example.events().count());

    assertEquals(Modifier.PUBLIC.mask(), example.modifiers());
    assertTrue(example.isPublic());
    assertFalse(example.isPrivate());
    assertFalse(example.isProtected());
    assertFalse(example.isStatic());

    DocumentationComment comment = example.documentationComment().orElseThrow();
    assertTrue(comment.summary().startsWith("An example application"));
  }

  @Test
  void test_field() {
    Field field = project.classes().findFirst().orElseThrow().fields().findFirst().orElseThrow();

    assertEquals("order", field.name());
    assertEquals("org.example.models.Order", field.type());
    assertNull(field.initializer());

    assertEquals(Modifier.PUBLIC.mask() | Modifier.READONLY.mask(), field.modifiers());
    assertTrue(field.isPublic());
    assertTrue(field.hasModifier(Modifier.READONLY));
  }

  @Test
  void test_method() {
    Method method = project.classes().findFirst().orElseThrow().methods().findFirst().orElseThrow();

    assertEquals("main", method.name());
    assertEquals("void", method.returnType());

    assertEquals(Modifier.PUBLIC.mask() | Modifier.STATIC.mask(), method.modifiers());
    assertTrue(method.isPublic());
    assertTrue(method.isStatic());

    Parameter parameter = method.parameters().findFirst().orElseThrow();

    assertEquals("args", parameter.name());
    assertEquals("java.lang.String[]", parameter.type());

    assertEquals(0, method.attributes().count());

    DocumentationComment comment = method.documentationComment().orElseThrow();
    assertTrue(comment.summary().startsWith("Command-line entry point"));
  }

  @Test
  void test_invocation() {
    Class cls = project.classes().findFirst().orElseThrow();
    Method mth = cls.methods().findFirst().orElseThrow();
    Statement stmt = mth.statements().findFirst().orElseThrow();

    Invocation invocation = assertInstanceOf(Invocation.class, stmt);

    assertEquals("publishEvent", invocation.name());
    assertEquals("org.springframework.context.ApplicationContext", invocation.containingType());

    Argument argument = invocation.arguments().findFirst().orElseThrow();

    assertEquals("org.example.events.OrderPaid", argument.type());
    assertEquals("new OrderPaid(new Order(6, \"123 Fake Street\"))", argument.text());
  }

  @Test
  void test_accept() {
    Visitor docCommentCounter = new BaseVisitor() {
      public int total = 0;

      @Override
      public void visitDocumentationComment(DocumentationComment documentationComment) {
        total++;
      }

      @Override
      public String toString() {
        return String.valueOf(total);
      }
    };

    project.accept(docCommentCounter);

    assertEquals("3", docCommentCounter.toString());
  }
}
