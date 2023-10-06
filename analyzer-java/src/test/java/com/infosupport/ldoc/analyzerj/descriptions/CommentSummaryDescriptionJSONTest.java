package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentSummaryDescriptionJSONTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void comment_summary_description_serializes_as_expected() throws IOException {
    String example = """
        {
          "Remarks": "tread carefully.",
          "Returns": "An integer.",
          "Summary": "add two values",
          "Params": {
            "N": "first integer value",
            "Y": "second integer value"
          }
        }
        """;
    assertEquals(
        mapper.readTree(example),
        mapper.valueToTree(
          new CommentSummaryDescription(
              "tread carefully.", "An integer.", "add two values",
              Map.of("N", "first integer value", "Y", "second integer value"),
              null)));
  }
}
