package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class IfDescriptionJSONTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void if_description_serializes_as_expected() throws IOException {
    String expected = """
        {
          "$type": "LivingDocumentation.If, LivingDocumentation.Statements",
          "Sections": [
            {
              "Condition": "true",
              "Statements": [
                {
                  "$type": "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions",
                  "Expression": "1"
                }
              ]
            },
            {
              "Condition": "false"
            },
            {
              "Statements": [
                {
                  "$type": "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions",
                  "Expression": "2"
                }
              ]
            }
          ]
        }
        """;

    assertEquals(
        mapper.readTree(expected),
        mapper.valueToTree(new IfDescription(List.of(
            new IfElseSection("true", List.of(new ReturnDescription("1"))),
            new IfElseSection("false", List.of()),
            new IfElseSection(null, List.of(new ReturnDescription("2")))))));
  }
}
