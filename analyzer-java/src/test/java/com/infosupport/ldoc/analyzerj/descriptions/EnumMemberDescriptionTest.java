package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EnumMemberDescriptionTest {

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
                    "Name" : "DUTCH"
                  }
                """;
    assertEquals(
        mapper.readTree(expected),
        mapper.valueToTree(
            new EnumMemberDescription(
                new MemberDescription("DUTCH", 0, List.of()),
                List.of(new ArgumentDescription("int", "5")),
                null)));
  }
}
