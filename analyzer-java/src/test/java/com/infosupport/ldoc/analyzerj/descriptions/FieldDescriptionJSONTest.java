package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldDescriptionJSONTest {


    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void field_description_serializes_as_expected() throws IOException {
        String expected = """
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
                                null)
                        )));
    }

    @Test
    void field_description_serializes_as_expected_no_initializer() throws IOException {
        String expected = """
                {
                "Type": "int",
                "Name": "name",
                "Modifiers": 4
                }
                """;
        assertEquals(
                mapper.readTree(expected),
                mapper.valueToTree(
                        new FieldDescription(
                                new MemberDescription("name", Modifier.PRIVATE.mask(), List.of()),
                                "int", null, null)));
    }
}
