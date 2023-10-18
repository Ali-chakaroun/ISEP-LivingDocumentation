package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MethodDescriptionJSONTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void method_description_serializes_as_expected() throws IOException {
    assertEquals(
        mapper.readTree("{\"Modifiers\": 4, \"Name\": \"aap\", \"ReturnType\": \"Noot\"}"),
        mapper.valueToTree(
            new MethodDescription(
                new MemberDescription("aap", Modifier.PRIVATE.mask(), List.of()),
                "Noot", null, List.of(), List.of())));

    String example = """
            {
              "Name": "mies",
              "ReturnType": "org.example.Gans",
              "DocumentationComments": {
                "Remarks": "Tread carefully.",
                "Returns": "An integer.",
                "Summary": "Add two values.",
                "Params": {
                   "N": "first integer value" ,
                   "Y": "second integer value"
                }
              },
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
                    new CommentSummaryDescription(
                        "Tread carefully.",
                        "An integer.",
                        "Add two values.",
                        Map.of(
                            "N", "first integer value",
                            "Y", "second integer value"),
                        null),
                List.of(
                    new ParameterDescription("Zeef", "gijs", List.of()),
                    new ParameterDescription("Muis", "jip", List.of())),
                List.of())));
  }

}
