package com.infosupport.ldoc.analyzerj;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.types.ResolvedType;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.TypeDescription;
import com.infosupport.ldoc.analyzerj.descriptions.TypeType;
import java.util.ArrayList;
import java.util.List;

public class AnalysisVisitor extends GenericListVisitorAdapter<Description, Analyzer> {

  private final SymbolResolver resolver;

  public AnalysisVisitor(SymbolResolver resolver) {
    this.resolver = resolver;
  }

  private List<String> resolve(List<ClassOrInterfaceType> types) {
    return types
        .stream()
        .map(t -> resolver.toResolvedType(t, ResolvedType.class).describe())
        .toList();
  }

  @Override
  public List<Description> visit(ClassOrInterfaceDeclaration n, Analyzer arg) {
    TypeType type = n.isInterface() ? TypeType.INTERFACE : TypeType.CLASS;
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = new ArrayList<>();
    baseTypes.addAll(resolve(n.getExtendedTypes()));
    baseTypes.addAll(resolve(n.getImplementedTypes()));

    return List.of(new TypeDescription(type, fullName, baseTypes));
  }

  @Override
  public List<Description> visit(RecordDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = resolve(n.getImplementedTypes());

    return List.of(new TypeDescription(TypeType.STRUCT /* approximately */, fullName, baseTypes));
  }

  @Override
  public List<Description> visit(EnumDeclaration n, Analyzer arg) {
    String fullName = n.getFullyQualifiedName().orElseThrow();
    List<String> baseTypes = resolve(n.getImplementedTypes());

    return List.of(new TypeDescription(TypeType.ENUM, fullName, baseTypes));
  }
}
