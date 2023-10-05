package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentSummaryDescriptionJSONTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void Check_for_comments() throws IOException {
        // Create a Map<String, String>
        Map<String, String> keyValueMap = new LinkedHashMap<>();

        // Add key-value pairs to the map
        keyValueMap.put("N", "first integer value");
        keyValueMap.put("Y", "second integer value");

        String example = """
        {
          "Summary": "add two values",
          "params": {\s
             "N": "first integer value" ,
             "Y": "second integer value" }
        }
        """;
        assertEquals(
                mapper.readTree(example),
                mapper.valueToTree(
                        new CommentSummaryDescription(null,null,"add two values",keyValueMap,null)));
    }
}
