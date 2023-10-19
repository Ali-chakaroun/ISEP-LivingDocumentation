package com.infosupport.ldoc.analyzerj.descriptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonnetQuirkTest {

  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Regression test for issue 28. In its default configuration, Json.NET expects its metadata (in
   * this case $type) to appear before other keys in the (otherwise unordered) map. Confirm that we
   * do this for all Description records that have a $type field.
   */
  @Test
  void type_metadata_comes_first() throws IOException {
    List<Description> descriptions = List.of(
        new AssignmentDescription("a", "=", "b"),
        new ForEachDescription("var x : xs", List.of()),
        new IfDescription(List.of()),
        new InvocationDescription("java.lang.Object", "toString", List.of()),
        new ReturnDescription("1"),
        new SwitchDescription("var", List.of()));

    for (Description description : descriptions) {
      String json = mapper.writeValueAsString(description);

      assertTrue(json.contains("\"$type\""), "JSON does not contain $type metadata: " + json);
      assertTrue(json.startsWith("{\"$type\":"), "JSON does not start with $type field: " + json);
    }
  }

}
