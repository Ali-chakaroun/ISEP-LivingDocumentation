package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class ReturnDescriptionJsonTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void return_description_serializes_as_expected() throws IOException {
    String plain = """
        {
          "$type": "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions"
        }
        """;
    String value = """
        {
          "$type": "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions",
          "Expression": "1"
        }
        """;

    assertEquals(mapper.readTree(plain), mapper.valueToTree(new ReturnDescription()));
    assertEquals(mapper.readTree(value), mapper.valueToTree(new ReturnDescription("1")));
  }
}
