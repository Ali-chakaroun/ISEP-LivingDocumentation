package com.infosupport.ldoc.analyzerj;

import java.nio.file.Path;

public class AnalysisJob {

  public final Path project;
  public final Path output;

  public AnalysisJob(Path project, Path output) {
    this.project = project;
    this.output = output;
  }
}
