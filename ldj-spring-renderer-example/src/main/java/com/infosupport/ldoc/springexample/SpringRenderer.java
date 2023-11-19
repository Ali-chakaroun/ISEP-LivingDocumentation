package com.infosupport.ldoc.springexample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SpringRenderer is an example renderer that makes Asciidoc with PlantUML diagrams from analyzed
 * Spring projects that use application events.
 */
public class SpringRenderer {

  /**
   * Reads LivingDocumentation JSON from the input stream and write Asciidoc to the output stream.
   */
  public void render(InputStream in, OutputStream out) throws IOException {
    in.transferTo(out);
  }

  /**
   * Command-line entry point. Reads JSON from standard input and writes Asciidoc to standard out.
   */
  public static void main(String[] args) throws IOException {
    new SpringRenderer().render(System.in, System.out);
  }
}
