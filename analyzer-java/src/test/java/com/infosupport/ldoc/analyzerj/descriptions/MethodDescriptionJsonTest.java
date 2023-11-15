package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class MethodDescriptionJsonTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void method_description_serializes_as_expected() throws IOException {
    assertEquals(
        mapper.readTree(
            "{\"Modifiers\": 4, \"Name\": \"aap\", \"ReturnType\": \"Noot\"}"),
        mapper.valueToTree(
            new MethodDescription(
                new MemberDescription("aap", Modifier.PRIVATE.mask(), List.of(), null),
                "Noot", List.of(), List.of())));

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
                    new ParameterDescription("Zeef", "gijs", List.of()),
                    new ParameterDescription("Muis", "jip", List.of())),
                List.of())));
  }

}
