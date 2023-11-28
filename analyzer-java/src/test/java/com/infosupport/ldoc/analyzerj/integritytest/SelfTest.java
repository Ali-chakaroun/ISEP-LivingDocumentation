package com.infosupport.ldoc.analyzerj.integritytest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infosupport.ldoc.analyzerj.Main;
import java.io.File;
import java.io.IOException;
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
    String[] args = {"--output", outputJson, "--project", projectDirectory};
    Main.main(args);
    File outputFile = new File(outputJson);
    assertTrue(outputFile.exists());
    outputFile.delete();
    assertFalse(outputFile.exists());
  }
}
