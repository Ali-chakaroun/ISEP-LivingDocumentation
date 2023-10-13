package com.infosupport.ldoc.mavenplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.analyzerj.AnalysisJob;
import com.infosupport.ldoc.analyzerj.Analyzer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "livingdocumentation", requiresDependencyResolution = ResolutionScope.COMPILE)
public class LivingDocumentationMojo extends AbstractMojo {

  @Parameter(readonly = true, defaultValue = "${project}")
  private MavenProject project;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    Analyzer a = new Analyzer(new ObjectMapper());
    Log log = getLog();

    try {
      Path proj = Path.of(project.getBuild().getSourceDirectory());
      Path target = Path.of(project.getBuild().getDirectory());
      Path out = target.resolve("livingdocumentation.json");
      Files.createDirectories(target);

      List<String> classpath = project.getCompileClasspathElements();

      log.info("Source path: " + proj);
      log.info("Output path: " + out);
      log.info("Class path:  " + String.join(":", classpath));

      a.analyze(new AnalysisJob(proj, out, classpath, true));
    } catch (IOException | DependencyResolutionRequiredException e) {
      throw new MojoFailureException(e);
    }
  }
}
