package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DocumentationCommentsDescriptionJsonTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void comment_summary_description_serializes_as_expected() throws IOException {
    String example = """
        {
          "Remarks": "tread carefully.\\r\\nWhen you code.",
          "Returns": "An integer.",
          "Summary": "add two values",
          "Params": {
            "N": "first integer value",
            "Y": "second integer value",
            "Map<input>":"map of strings."
          },
          "TypeParams": {
            "L": "of type list.",
            "L<C>": "list of characters."
          }
        }
        """;
    assertEquals(
        mapper.readTree(example),
        mapper.valueToTree(
            new DocumentationCommentsDescription(
                "tread carefully.\r\nWhen you code.", "An integer.", "add two values",
                Map.of("N", "first integer value", "Y", "second integer value",
                    "Map<input>", "map of strings."),
                Map.of("L", "of type list.", "L<C>", "list of characters."))));
  }
}
