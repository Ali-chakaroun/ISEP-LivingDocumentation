package com.infosupport.ldoc.gradleplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.analyzerj.AnalysisJob;
import com.infosupport.ldoc.analyzerj.Analyzer;
import java.io.File;
import java.io.IOException;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.CompileClasspath;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

/**
 * LivingDocumentationTask runs the Java analyzer on the input source directory (if it has changed).
 */
public abstract class LivingDocumentationTask extends DefaultTask {

  @InputDirectory
  public abstract DirectoryProperty getInputDirectory();

  @InputFiles
  @CompileClasspath
  public abstract ConfigurableFileCollection getCompileClasspath();

  @OutputFile
  public abstract RegularFileProperty getOutputFile();

  /**
   * Analyzes the input directory.
   */
  @TaskAction
  public void perform() throws IOException {
    AnalysisJob job = new AnalysisJob(
        getInputDirectory().getAsFile().get().toPath(),
        getOutputFile().getAsFile().get().toPath(),
        getCompileClasspath().getFiles().stream().map(File::getPath).toList(),
        false);
    new Analyzer(new ObjectMapper()).analyze(job);
  }
}
