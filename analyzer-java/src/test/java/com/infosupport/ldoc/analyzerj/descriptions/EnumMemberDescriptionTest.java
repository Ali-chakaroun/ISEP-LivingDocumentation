package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EnumMemberDescriptionTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void field_description_serializes_as_expected() throws IOException {
    String expected =
        """
            {
                "Arguments" : [ {
                  "Type" : "int",
                  "Text" : "5"
                } ],
                "Name" : "DUTCH",
                "DocumentationComments": {
                   "Remarks": "Tread carefully.",
                   "Returns": "An integer.",
                   "Summary": "Add two values.",
                   "Params": {
                        "N": "first integer value" ,
                        "Y": "second integer value"
                   },
                   "TypeParams": {
                        "L": "of type list.",
                        "L<C>": "list of characters."
                   }
                }
              }
            """;
    assertEquals(
        mapper.readTree(expected),
        mapper.valueToTree(
            new EnumMemberDescription(
                new MemberDescription("DUTCH", 0, List.of(), new DocumentationCommentsDescription(
                    "Tread carefully.",
                    "An integer.",
                    "Add two values.",
                    Map.of(
                        "N", "first integer value",
                        "Y", "second integer value"),
                    Map.of("L", "of type list.", "L<C>", "list of characters."))),
                List.of(new ArgumentDescription("int", "5")))));
  }
}
