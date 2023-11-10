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
        mapper.readTree("{\"FullName\": \"com.example.Spam\"}"),
        mapper.valueToTree(new TypeDescription(TypeType.CLASS, "com.example.Spam", List.of())));

    assertEquals(
        mapper.readTree("{\"FullName\": \"foo.bar.Baz\", \"BaseTypes\": [\"snork\"]}"),
        mapper.valueToTree(new TypeDescription(TypeType.CLASS, "foo.bar.Baz", List.of("snork"))));

    assertEquals(
        mapper.readTree("{\"Type\": 1, \"FullName\": \"Fred\"}"),
        mapper.valueToTree(new TypeDescription(TypeType.INTERFACE, "Fred", List.of())));

    assertEquals(
        mapper.readTree("{\"Type\": 2, \"FullName\": \"Jim\"}"),
        mapper.valueToTree(new TypeDescription(TypeType.STRUCT, "Jim", List.of())));

    assertEquals(
        mapper.readTree("{\"Type\": 3, \"FullName\": \"Barney\"}"),
        mapper.valueToTree(new TypeDescription(TypeType.ENUM, "Barney", List.of())));

    assertEquals(
        mapper.readTree("{\"Modifiers\": 1026, \"FullName\": \"Wilma\"}"),
        mapper.valueToTree(
            new TypeDescription.Builder(TypeType.CLASS, "Wilma")
                .withModifiers(Modifier.PUBLIC.mask() | Modifier.SEALED.mask())
                .build()));
  }
}
