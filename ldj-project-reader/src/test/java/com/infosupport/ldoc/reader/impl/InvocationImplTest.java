package com.infosupport.ldoc.reader.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.infosupport.ldoc.reader.Invocation;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Project;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class InvocationImplTest {

  @Test
  void invokedMethod() throws IOException {
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
}
