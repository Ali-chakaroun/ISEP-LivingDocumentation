package com.infosupport.ldoc.gradleplugin;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;

/**
 * LivingDocumentationPlugin adds Living Documentation JSON generation tasks to the first source
 * directory of every Java source set in the project.
 */
@NonNullApi
public class LivingDocumentationPlugin implements Plugin<Project> {

  private static final String OUTPUT_FILENAME = "livingdocumentation.json";

  @Override
  public void apply(Project project) {
    project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets().all(set -> {
      if (SourceSet.isMain(set)) {
        String name = set.getTaskName("generate", "LivingDocumentation");

        project.getTasks().register(name, LivingDocumentationTask.class, task -> {
          task.getCompileClasspath().setFrom(set.getCompileClasspath());
          task.getInputDirectory().set(set.getJava().getSourceDirectories().iterator().next());
          task.getOutputFile().set(project.getLayout().getBuildDirectory().file(OUTPUT_FILENAME));
        });

        project.getTasks().named(set.getClassesTaskName()).configure(task -> task.dependsOn(name));
      }
    });
  }
}
