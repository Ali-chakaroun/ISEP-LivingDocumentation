package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class MethodDescriptionJSONTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void method_description_serializes_as_expected() throws IOException {
    assertEquals(
        mapper.readTree("{\"Name\": \"aap\", \"ReturnType\": \"Noot\"}"),
        mapper.valueToTree(
            new MethodDescription(new MemberDescription("aap"), "Noot", List.of(), List.of())));

    String example = """
        {
          "Name": "mies",
          "ReturnType": "org.example.Gans",
          "Parameters": [
            { "Name": "gijs", "Type": "Zeef" },
            { "Name": "jip", "Type": "Muis" }
          ]
        }
        """;
    assertEquals(
        mapper.readTree(example),
        mapper.valueToTree(
            new MethodDescription(
                new MemberDescription("mies"),
                "org.example.Gans",
                List.of(
                    new ParameterDescription("Zeef", "gijs"),
                    new ParameterDescription("Muis", "jip")),
                List.of())));
  }

}
