package com.infosupport.ldoc.analyzerj;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.TypeDescription;
import java.util.List;
import java.util.stream.Stream;

public class AnalysisVisitor extends GenericListVisitorAdapter<Description, Analyzer> {

  @Override
  public List<Description> visit(ClassOrInterfaceDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = Stream
        .concat(n.getExtendedTypes().stream(), n.getImplementedTypes().stream())
        .map(ClassOrInterfaceType::resolve)
        .map(ResolvedType::describe)
        .toList();
    return List.of(new TypeDescription(fullName, baseTypes));
  }
}
