package com.infosupport.ldoc.analyzerj;

import java.nio.file.Path;
import java.util.List;

public record AnalysisJob(Path project, Path output, List<String> classpath, boolean pretty) {

}
