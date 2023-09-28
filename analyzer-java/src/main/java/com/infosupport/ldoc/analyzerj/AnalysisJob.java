package com.infosupport.ldoc.analyzerj;

import java.nio.file.Path;

public record AnalysisJob(Path project, Path output, boolean pretty) {

}
