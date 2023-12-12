package com.infosupport.ldoc.support;

public interface Visitor {

  void visitClass(Class klass);

  void visitMethod(Method method);

  void visitProject(Project project);

  void visitType(Type type);
}
