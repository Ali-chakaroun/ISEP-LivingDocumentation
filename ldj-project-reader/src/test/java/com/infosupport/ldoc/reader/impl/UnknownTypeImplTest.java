package com.infosupport.ldoc.reader.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.infosupport.ldoc.reader.Example;
import com.infosupport.ldoc.reader.Project;
import com.infosupport.ldoc.reader.Type;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class UnknownTypeImplTest {

  @Test
  void test() throws IOException {
    ObjectMapper om = new ObjectMapper();
    JsonNode tree = om.readTree(Example.class.getResource("example.json"));
    ArrayNode arr = assertInstanceOf(ArrayNode.class, tree);
    ObjectNode obj = assertInstanceOf(ObjectNode.class, arr.get(0));
    Project proj = new ProjectImpl(om, tree);

    // Given that the example JSON contains one class...
    assertEquals(1, proj.allTypes().count());
    assertEquals(1, proj.classes().count());

    // ...when we mutate the JSON to introduce a bogus Type value...
    obj.put("Type", -1);

    // ...then the example no longer contains a class, but instead some unknown type.
    assertEquals(1, proj.allTypes().count());
    assertEquals(0, proj.classes().count());

    Type type = proj.allTypes().findFirst().orElseThrow();
    assertInstanceOf(UnknownTypeImpl.class, type);

    // The unknown type should be usable as a normal type...
    assertEquals("EventApp", type.name());

    // ...but it should be skipped by visitors.
    type.accept(null);
  }
}
