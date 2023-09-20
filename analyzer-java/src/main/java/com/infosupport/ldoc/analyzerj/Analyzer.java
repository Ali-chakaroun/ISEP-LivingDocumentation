package com.infosupport.ldoc.analyzerj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Analyzer {

  private final ObjectMapper objectMapper;

  public Analyzer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public void analyze(AnalysisJob job) throws IOException {
    ProjectRoot projectRoot = new SymbolSolverCollectionStrategy().collect(job.project);
    List<Description> descriptions = new ArrayList<>();

    for (SourceRoot sourceRoot : projectRoot.getSourceRoots()) {
      List<ParseResult<CompilationUnit>> results = sourceRoot.tryToParse();

      for (ParseResult<CompilationUnit> result : results) {
        AnalysisVisitor visitor = new AnalysisVisitor();
        CompilationUnit compilationUnit = result.getResult().orElseThrow();

        descriptions.addAll(compilationUnit.accept(visitor, this));
      }
    }

    objectMapper.writeValue(job.output.toFile(), descriptions);
  }
}
