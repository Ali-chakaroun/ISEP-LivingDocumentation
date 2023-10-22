package com.infosupport.ldoc.analyzerj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.TypeSolverBuilder;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.CollectionStrategy;
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

  private TypeSolver typeSolverFor(AnalysisJob job) throws IOException {
    var typeSolverBuilder = new TypeSolverBuilder().withSourceCode(job.project());

    for (String cp : job.classpath()) {
      if (cp.toLowerCase().endsWith(".jar")) {
        typeSolverBuilder = typeSolverBuilder.withJAR(cp);
      }
    }

    return typeSolverBuilder.withCurrentClassloader().build();
  }

  public void analyze(AnalysisJob job) throws IOException {
    ParserConfiguration parserConfiguration = new ParserConfiguration()
        .setSymbolResolver(new JavaSymbolSolver(typeSolverFor(job)));
    CollectionStrategy strategy = new SymbolSolverCollectionStrategy(parserConfiguration);
    List<Description> descriptions = new ArrayList<>();

    for (SourceRoot sourceRoot : strategy.collect(job.project()).getSourceRoots()) {
      List<ParseResult<CompilationUnit>> results = sourceRoot.tryToParse();
      SymbolResolver solver = sourceRoot.getParserConfiguration().getSymbolResolver().orElseThrow();

      for (ParseResult<CompilationUnit> result : results) {
        CompilationUnit compilationUnit = result.getResult().orElseThrow();
        AnalysisVisitor visitor = new AnalysisVisitor(solver);

        List<Description> visited = compilationUnit.accept(visitor, this);
        descriptions.addAll(visited);
      }
    }

    ObjectWriter writer = job.pretty()
        ? objectMapper.writerWithDefaultPrettyPrinter()
        : objectMapper.writer();
    writer.writeValue(job.output().toFile(), descriptions);
  }
}
