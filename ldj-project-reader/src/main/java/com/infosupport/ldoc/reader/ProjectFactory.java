package com.infosupport.ldoc.reader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

/**
 * Factory to construct a project node.
 */
public interface ProjectFactory {

  /**
   * Create project node.
   *
   * @param file input JSON file.
   * @return Project node.
   * @throws IOException for invalid input.
   */
  Project project(File file) throws IOException;

  /**
   * Create project node.
   *
   * @param string input JSON string.
   * @return Project node.
   * @throws IOException for invalid input.
   */
  Project project(String string) throws IOException;

  /**
   * Create project node.
   *
   * @param reader input JSON reader.
   * @return Project node.
   * @throws IOException for invalid input.
   */
  Project project(Reader reader) throws IOException;

  /**
   * Create project node.
   *
   * @param url input JSON url.
   * @return Project node.
   * @throws IOException for invalid input.
   */
  Project project(URL url) throws IOException;
}
