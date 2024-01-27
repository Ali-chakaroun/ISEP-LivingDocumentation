package com.infosupport.ldoc.reader.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.infosupport.ldoc.reader.Invocation;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Project;
import com.infosupport.ldoc.reader.visitor.BaseVisitor;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class InvocationImplTest {

  @Test
  void invokedMethodBasic() throws IOException {
    /* Given that we have a class like this...

    class InvocationExample {
      void foo() { bar(); }
      void bar() { }
    }
     */
    String example = """
        [ {
         "FullName": "org.example.InvocationExample",
         "Methods": [ {
           "ReturnType": "void",
           "Statements": [ {
             "$type": "LivingDocumentation.InvocationDescription, LivingDocumentation.Descriptions",
             "ContainingType": "org.example.InvocationExample",
             "Name": "bar"
           } ],
           "Name": "foo"
         }, {
           "ReturnType": "void",
           "Name": "bar"
         } ]
        } ]
          """;

    Project project = new JacksonProjectFactory().project(example);

    /* ...when we look up the invoked method for that invocation... */
    Invocation invoke = project
        .classes().findFirst()
        .flatMap(klass -> klass.methodsWithName("foo").findFirst())
        .flatMap(method -> method.statements().findFirst())
        .map(Invocation.class::cast)
        .orElseThrow();

    Method target = invoke.getInvokedMethod().orElseThrow();

    /* ...then we indeed find 'bar'. */
    assertEquals("bar", target.name());
  }

  @Test
  void invokedMethodOverloaded() throws IOException {
    /* Given that we have a class like the above, but with multiple overloads for bar... */
    String example = """
        [ {
         "FullName": "org.example.InvocationExample",
         "Methods": [ {
           "ReturnType": "void",
           "Statements": [ {
             "$type": "LivingDocumentation.InvocationDescription, LivingDocumentation.Descriptions",
             "ContainingType": "org.example.InvocationExample",
             "Name": "bar",
             "Arguments": [ { "Value": "12", "Type": "int" } ]
           } ],
           "Name": "foo"
         }, {
           "ReturnType": "void",
           "Name": "bar",
           "Parameters": [ { "Name": "number", "Type": "int" }, { "Name": "two", "Type": "int" }]
         }, {
           "ReturnType": "void",
           "Name": "bar",
           "Parameters": [ {"Name": "text", "Type": "java.lang.String"} ]
         }, {
           "ReturnType": "void",
           "Name": "bar",
           "Parameters": [ { "Name": "number", "Type": "int" } ],
           "Attributes": [ { "Type": "Foo", "Name": "Bar" } ]
         } ]
        } ]
          """;

    Project project = new JacksonProjectFactory().project(example);

    project.accept(new BaseVisitor() {
      @Override
      public void visitInvocation(Invocation invocation) {
        /* ...when we look up the invoked method for that invocation, we find the one... */
        Method target = invocation.getInvokedMethod().orElseThrow();

        /* ...with the attribute. */
        assertEquals(1, target.attributes().count());
      }
    });
  }
}
