package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FieldDescriptionJsonTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void field_description_serializes_as_expected() throws IOException {
    String expected =
        """
            {
            "Type": "java.lang.String",
            "Initializer": "Hai",
            "Name": "name",
            "Modifiers": 4,
            "DocumentationComments": {
                "Remarks": "Tread carefully.",
                "Returns": "An integer.",
                "Summary": "Add two values.",
                "Params": {
                    "N": "first integer value" ,
                    "Y": "second integer value"
                    }
                }
            }
            """;
    assertEquals(
        mapper.readTree(expected),
        mapper.valueToTree(
            new FieldDescription(
                new MemberDescription("name", Modifier.PRIVATE.mask(), List.of()),
                "java.lang.String",
                "Hai",
                new CommentSummaryDescription(
                    "Tread carefully.",
                    "An integer.",
                    "Add two values.",
                    Map.of(
                        "N", "first integer value",
                        "Y", "second integer value"),
                    null))));
  }

  @Test
  void field_description_serializes_as_expected_no_initializer() throws IOException {
    String expected =
        """
            {
            "Type": "int",
            "Name": "name",
            "Modifiers": 4,
            "DocumentationComments": {}
            }
            """;
    assertEquals(
        mapper.readTree(expected),
        mapper.valueToTree(
            new FieldDescription(
                new MemberDescription("name", Modifier.PRIVATE.mask(), List.of()),
                "int",
                null,
                new CommentSummaryDescription())));
  }

  @Test
  void initializer_is_method_call() throws IOException {
    String expected =
        """
            {
            "Type": "float",
            "Name": "name",
            "Modifiers": 4,
            "Initializer": "System.currentTimeMillis()",
            "DocumentationComments": {}
            }
            """;
    assertEquals(
        mapper.readTree(expected),
        mapper.valueToTree(
            new FieldDescription(
                new MemberDescription("name", Modifier.PRIVATE.mask(), List.of()),
                "float",
                "System.currentTimeMillis()",
                new CommentSummaryDescription())));
  }


}
