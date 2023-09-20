package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class TypeDescriptionJSONTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void type_description_serializes_as_expected() throws IOException {
    assertEquals(
        mapper.valueToTree(new TypeDescription("com.example.spam", List.of())),
        mapper.readTree("{\"FullName\": \"com.example.spam\"}"));

    assertEquals(
        mapper.valueToTree(new TypeDescription("com.example.eggs", List.of("snork"))),
        mapper.readTree("{\"FullName\": \"com.example.eggs\", \"BaseTypes\": [\"snork\"]}"));
  }
}
