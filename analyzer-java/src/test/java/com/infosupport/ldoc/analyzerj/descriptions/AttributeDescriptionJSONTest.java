package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class AttributeDescriptionJSONTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void attribute_description_serializes_as_expected() throws IOException {
    String marker = """
          {
            "Type": "java.lang.Override",
            "Name": "Override"
          }
        """;
    assertEquals(
        mapper.readTree(marker),
        mapper.valueToTree(new AttributeDescription("java.lang.Override", "Override", List.of())));

    String normal = """
          {
            "Type": "org.example.SomeAnnotation",
            "Name": "SomeAnnotation",
            "Arguments": [
              {
                "Name": "value",
                "Type": "java.lang.String",
                "Value": "\\"hello\\""
              }
            ]
          }
        """;
    assertEquals(
        mapper.readTree(normal),
        mapper.valueToTree(new AttributeDescription(
            "org.example.SomeAnnotation",
            "SomeAnnotation",
            List.of(
                new AttributeArgumentDescription("value", "java.lang.String", "\"hello\"")))));
  }
}
