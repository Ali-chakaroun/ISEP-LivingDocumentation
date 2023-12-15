package com.infosupport.ldoc.reader;

public interface Visitor {

  void visitClass(Class klass);

  void visitMethod(Method method);

  void visitProject(Project project);

  void visitField(Field field);

  void visitConstructor(Constructor constructor);

  void visitProperty(Property property);
}
