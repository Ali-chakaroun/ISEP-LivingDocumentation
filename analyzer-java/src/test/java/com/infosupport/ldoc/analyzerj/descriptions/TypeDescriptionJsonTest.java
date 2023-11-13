package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class TypeDescriptionJsonTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void type_description_serializes_as_expected() throws IOException {
    assertEquals(
        mapper.readTree("{\"FullName\": \"com.example.Spam\", \"DocumentationComments\": {}}"),
        mapper.valueToTree(new TypeDescription(TypeType.CLASS, "com.example.Spam")));

    assertEquals(
        mapper.readTree(
            "{\"FullName\": \"foo.bar.Baz\", \"BaseTypes\": [\"snork\"],"
                + " \"DocumentationComments\": {}}"),
        mapper.valueToTree(new TypeDescription(TypeType.CLASS, "foo.bar.Baz", List.of("snork"))));

    assertEquals(
        mapper.readTree("{\"Type\": 1, \"FullName\": \"Fred\", \"DocumentationComments\": {}}"),
        mapper.valueToTree(new TypeDescription(TypeType.INTERFACE, "Fred", List.of())));

    assertEquals(
        mapper.readTree("{\"Type\": 2, \"FullName\": \"Jim\", \"DocumentationComments\": {}}"),
        mapper.valueToTree(new TypeDescription(TypeType.STRUCT, "Jim", List.of())));

    assertEquals(
        mapper.readTree("{\"Type\": 3, \"FullName\": \"Barney\", \"DocumentationComments\": {}}"),
        mapper.valueToTree(new TypeDescription(TypeType.ENUM, "Barney", List.of())));

    assertEquals(
        mapper.readTree(
            "{\"Modifiers\": 1026, \"FullName\": \"Wilma\", \"DocumentationComments\": {}}"),
        mapper.valueToTree(new TypeDescription(
            TypeType.CLASS,
            Modifier.PUBLIC.mask() | Modifier.SEALED.mask(),
            "Wilma",
            List.of(),
            new CommentSummaryDescription(),
            List.of(),
            List.of(),
            List.of(),
            List.of(), List.of())));
  }
}
