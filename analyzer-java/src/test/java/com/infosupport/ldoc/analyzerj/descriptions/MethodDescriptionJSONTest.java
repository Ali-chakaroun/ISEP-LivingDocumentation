package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MethodDescriptionJSONTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void method_description_serializes_as_expected() throws IOException {
    // Create a Map<String, String>
    Map<String, String> keyValueMap = new LinkedHashMap<>();

    // Add key-value pairs to the map
    keyValueMap.put("N", "first integer value");
    keyValueMap.put("Y", "second integer value");

    assertEquals(
        mapper.readTree("{\"Name\": \"aap\", \"ReturnType\": \"Noot\"}"),
        mapper.valueToTree(
            new MethodDescription(new MemberDescription("aap"), "Noot",null,List.of(), List.of())));

    String example = """
            {
              "Name": "mies",
              "ReturnType": "org.example.Gans",
              "DocumentationComments": {
               "Summary": "add two values",
                  "params": {
                     "N": "first integer value" ,
                     "Y": "second integer value" }
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
                    new CommentSummaryDescription(null,null,"add two values",keyValueMap,null),
                List.of(
                    new ParameterDescription("Zeef", "gijs", List.of()),
                    new ParameterDescription("Muis", "jip", List.of())),
                List.of())));
  }

}
