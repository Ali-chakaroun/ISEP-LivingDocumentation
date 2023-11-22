package com.infosupport.ldoc.analyzerj.integritytest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.infosupport.ldoc.analyzerj.Main;
import java.io.File;
import java.io.IOException;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;
import org.junit.jupiter.api.Test;

class SelfTest {

  @Test
  @SuppressWarnings("ResultOfMethodCallIgnored")
  void testMain() throws IOException {
    String outputJson = "parserTest.json";
    // returns path..\ISEP-LivingDocumentation\analyzer-java
    String projectRoot = System.getProperty("user.dir");
    String projectDirectory =
        projectRoot + File.separator + "src" + File.separator + "main" + File.separator + "java";
    String schemaPath = projectRoot + File.separator + ".." + File.separator + "schema.json";
    String[] args = {"--output", outputJson, "--project", projectDirectory};
    Main.main(args);
    File outputFile = new File(outputJson);
    assertTrue(outputFile.exists());
    String jsonPath = outputFile.getPath();
    try {
      SchemaStore schemaStore = new SchemaStore();
      Schema schema = schemaStore.loadSchema(new File(schemaPath));
      Validator validator = new Validator();
      validator.validate(schema, new File(jsonPath));
    }  catch (SchemaException | IOException e) {
      e.printStackTrace();
      fail("Json file not supported by the defined Json schema");
    }
    outputFile.delete();
    assertFalse(outputFile.exists());
  }
}
