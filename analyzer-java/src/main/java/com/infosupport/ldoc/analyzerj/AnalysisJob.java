package com.infosupport.ldoc.analyzerj;

import java.nio.file.Path;
import java.util.List;

/**
 * Parameter object describing a project to be analyzed.
 *
 * @param project   directory to scan for source files, like <code>/path/to/src/main/java</code>
 * @param output    file path where the resulting JSON should be written
 * @param classpath absolute paths to JARs that are added to the classpath for type resolution
 * @param pretty    whether to indent the resulting JSON
 */
public record AnalysisJob(Path project, Path output, List<String> classpath, boolean pretty) {

}
