package com.infosupport.ldoc.reader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infosupport.ldoc.reader.impl.JacksonProjectFactory;
import com.infosupport.ldoc.reader.visitor.BaseVisitor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests with kitchensink.json, an output from the C# analyzer that covers the entire JSON Schema as
 * much as possible.
 */
class KitchenSinkJsonTest {

  private final Project project;

  KitchenSinkJsonTest() throws IOException {
    URL url = KitchenSinkJsonTest.class.getResource("kitchensink.json");
    project = new JacksonProjectFactory().project(url);
  }

  @Test
  void baseVisitSucceeds() {
    /* Given that we don't override anything in the BaseVisitor, it does not complain. */
    assertDoesNotThrow(() -> project.accept(new BaseVisitor() {
    }));
  }

  @Test
  void typesByKind() {
    /* Given that some classes, enums, interfaces and structs exist, we can find them. */
    assertEquals(
        List.of("KitchenSink", "SomeClass"),
        project.classes().map(HasName::name).toList());
    assertEquals(List.of("ExampleEnum"), project.enums().map(HasName::name).toList());
    assertEquals(List.of("IExampleInterface"), project.interfaces().map(HasName::name).toList());
    assertEquals(List.of("ExampleStruct"), project.structs().map(HasName::name).toList());
  }

  @Test
  void baseTypes() {
    Type struct = project.type("ExampleStruct").orElseThrow();
    struct.baseTypes().forEach(b -> project.type(b).orElseThrow());
  }

  @Test
  void documentationComment() {
    /* Exactly one documentation comment exists, and when read, it matches our expectations. */
    List<DocumentationComment> seen = new ArrayList<>();

    project.accept(new BaseVisitor() {
      @Override
      public void visitDocumentationComment(DocumentationComment doc) {
        assertEquals("Summary goes here.", doc.summary());
        assertEquals("Remarks go here.", doc.remarks());
        assertEquals("Returned value goes here.", doc.returns());
        assertEquals(Map.of("args", "Description for args goes here."), doc.params());
        assertEquals(Set.of("EverythingApp.ExampleEnum"), doc.seeAlsos().keySet());
        assertEquals(
            Map.of("System.IO.IOException", "IOException description goes here."),
            doc.exceptions());

        /* Missing tags are null. */
        assertNull(doc.example());
        assertNull(doc.value());
        assertNull(doc.typeParams());
        assertNull(doc.permissions());

        seen.add(doc);
      }
    });

    assertEquals(1, seen.size());
  }

  @Test
  void attribute() {
    /* Exactly one attribute exists in the entire project and it has exactly the shape we expect. */
    List<Attribute> seen = new ArrayList<>();

    project.accept(new BaseVisitor() {
      @Override
      public void visitAttribute(Attribute attrib) {
        assertEquals("Obsolete", attrib.name());
        assertEquals("System.ObsoleteAttribute", attrib.type());

        assertEquals(1, attrib.arguments().peek(arg -> {
          assertEquals("Example attribute", arg.value());
          assertEquals("string", arg.type());
        }).count());

        seen.add(attrib);
      }
    });

    project.accept(new BaseVisitor() {
      @Override
      public void visitMethod(Method method) {
        if (method.name().equals("Example")) {
          assertEquals(1, method.attributes().count());
          assertEquals(1, method.attributesOfType("System.ObsoleteAttribute").count());
          assertEquals(0, method.attributesOfType("bogus type").count());
          assertEquals(0, method.attributesOfType(null).count());

          assertTrue(method.hasAttribute("System.ObsoleteAttribute"));
          assertFalse(method.hasAttribute("bogus type"));
        }
      }
    });

    assertEquals(1, seen.size());
  }
}
