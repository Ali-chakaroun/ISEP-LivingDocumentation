package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Statement;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class Util {

  static <T> Stream<T> streamOf(JsonNode node, Function<JsonNode, T> converter) {
    return StreamSupport.stream(node.spliterator(), false).map(converter);
  }

  static Stream<Statement> statements(ProjectImpl project, JsonNode node) {
    return streamOf(node.path("Statements"), s -> {
      return switch (s.path("$type").textValue().split(", ")[0]) {
        case "LivingDocumentation.AssignmentDescription" -> new AssignmentImpl(s);
        case "LivingDocumentation.ForEach" -> new ForEachImpl(project, s);
        case "LivingDocumentation.If" -> new IfImpl(project, s);
        case "LivingDocumentation.InvocationDescription" -> new InvocationImpl(project, s);
        case "LivingDocumentation.ReturnDescription" -> new ReturnImpl(s);
        case "LivingDocumentation.Switch" -> new SwitchImpl(project, s);
        default -> new UnknownStatementImpl();
      };
    });
  }
}
