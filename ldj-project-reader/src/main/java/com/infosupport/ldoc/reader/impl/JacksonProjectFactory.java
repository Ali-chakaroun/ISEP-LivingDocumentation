package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.reader.Project;
import com.infosupport.ldoc.reader.ProjectFactory;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

public class JacksonProjectFactory implements ProjectFactory {

  private final ObjectMapper om;

  public JacksonProjectFactory() {
    this(new ObjectMapper());
  }

  public JacksonProjectFactory(ObjectMapper objectMapper) {
    this.om = objectMapper;
  }

  private Project project(JsonNode node) {
    return new ProjectImpl(om, node);
  }

  @Override
  public Project project(File file) throws IOException {
    return project(om.readTree(file));
  }

  @Override
  public Project project(String string) throws IOException {
    return project(om.readTree(string));
  }

  @Override
  public Project project(Reader reader) throws IOException {
    return project(om.readTree(reader));
  }

  @Override
  public Project project(URL url) throws IOException {
    return project(om.readTree(url));
  }
}
