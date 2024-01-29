package com.infosupport.ldoc.reader;

/**
 * Visitor interface to implement for use with Project.accept().
 */
public interface Visitor {

  void visitArgument(Argument argument);

  void visitAssignment(Assignment assignment);

  void visitAttribute(Attribute attribute);

  void visitAttributeArgument(AttributeArgument attributeArgument);

  void visitClass(Class classType);

  void visitConstructor(Constructor constructor);

  void visitDocumentationComment(DocumentationComment documentationComment);

  void visitEnum(Enum enumType);

  void visitEnumMember(EnumMember enumMember);

  void visitEvent(Event event);

  void visitField(Field field);

  void visitForEach(ForEachStatement forEachStatement);

  void visitIf(IfStatement ifStatement);

  void visitIfElseSection(IfElseSection ifElseSection);

  void visitInterface(Interface interfaceType);

  void visitInvocation(Invocation invocation);

  void visitMethod(Method method);

  void visitParameter(Parameter parameter);

  void visitProject(Project project);

  void visitProperty(Property property);

  void visitReturn(ReturnStatement returnStatement);

  void visitStruct(Struct structType);

  void visitSwitch(SwitchStatement switchStatement);

  void visitSwitchSection(SwitchSection switchSection);
}
