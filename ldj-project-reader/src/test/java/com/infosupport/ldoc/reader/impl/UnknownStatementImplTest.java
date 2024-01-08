package com.infosupport.ldoc.reader.impl;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.reader.Example;
import com.infosupport.ldoc.reader.Project;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class UnknownStatementImplTest {

  @Test
  void test() throws IOException {
    ObjectMapper om = new ObjectMapper();
    JsonNode tree = om.readTree(Example.class.getResource("example.json"));
    Project proj = new ProjectImpl(om, tree);

    // Given that we mutate the JSON so that the only statement now has an unknown type...
    tree.withObject("/0/Methods/0/Statements/0").put("$type", "no such type");

    proj.classes().forEach(cls -> {
      cls.methods().forEach(mth -> {
        mth.statements().forEach(stmt -> {
          // ...then that is represented as an UnknownStatement...
          assertInstanceOf(UnknownStatementImpl.class, stmt);

          // ...which is ignored by visitors.
          stmt.accept(null);
        });
      });
    });
  }
}
