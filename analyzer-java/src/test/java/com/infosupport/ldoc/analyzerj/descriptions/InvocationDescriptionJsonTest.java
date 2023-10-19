package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvocationDescriptionJsonTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void invocation_description_serializes_as_expected() throws IOException {
    String expected = """
        {
          "$type": "LivingDocumentation.InvocationDescription, LivingDocumentation.Descriptions",
          "ContainingType": "java.util.ArrayList<Integer>",
          "Name": "add",
          "Arguments": [
            {
              "Type": "int",
              "Text": "1"
            }
          ]
        }
        """;

    assertEquals(
        mapper.readTree(expected),
        mapper.valueToTree(new InvocationDescription(
            "java.util.ArrayList<Integer>", "add", List.of(new ArgumentDescription("int", "1")))));
  }
}
