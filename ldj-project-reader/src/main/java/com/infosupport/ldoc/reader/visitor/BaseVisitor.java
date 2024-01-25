package com.infosupport.ldoc.reader.visitor;

import com.infosupport.ldoc.reader.Argument;
import com.infosupport.ldoc.reader.Assignment;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.AttributeArgument;
import com.infosupport.ldoc.reader.Class;
import com.infosupport.ldoc.reader.Constructor;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.Enum;
import com.infosupport.ldoc.reader.EnumMember;
import com.infosupport.ldoc.reader.Event;
import com.infosupport.ldoc.reader.Field;
import com.infosupport.ldoc.reader.ForEachStatement;
import com.infosupport.ldoc.reader.IfElseSection;
import com.infosupport.ldoc.reader.IfStatement;
import com.infosupport.ldoc.reader.Interface;
import com.infosupport.ldoc.reader.Invocation;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Parameter;
import com.infosupport.ldoc.reader.Project;
import com.infosupport.ldoc.reader.Property;
import com.infosupport.ldoc.reader.ReturnStatement;
import com.infosupport.ldoc.reader.Struct;
import com.infosupport.ldoc.reader.SwitchSection;
import com.infosupport.ldoc.reader.SwitchStatement;
import com.infosupport.ldoc.reader.Type;
import com.infosupport.ldoc.reader.Visitor;

/**
 * Visitor class that visits everything by default.
 */
public abstract class BaseVisitor implements Visitor {

  @Override
  public void visitArgument(Argument argument) {
  }

  @Override
  public void visitAssignment(Assignment assignment) {
  }

  @Override
  public void visitAttribute(Attribute attribute) {
    attribute.arguments().forEach(a -> a.accept(this));
  }

  @Override
  public void visitAttributeArgument(AttributeArgument attributeArgument) {
  }

  private void visitType(Type type) {
    type.fields().forEach(f -> f.accept(this));
    type.attributes().forEach(a -> a.accept(this));
    type.constructors().forEach(c -> c.accept(this));
    type.methods().forEach(m -> m.accept(this));
    type.events().forEach(e -> e.accept(this));
    type.enumMembers().forEach(m -> m.accept(this));
    type.properties().forEach(p -> p.accept(this));
    type.documentationComment().ifPresent(d -> d.accept(this));
  }

  @Override
  public void visitClass(Class classType) {
    visitType(classType);
  }

  @Override
  public void visitConstructor(Constructor constructor) {
    constructor.attributes().forEach(a -> a.accept(this));
    constructor.parameters().forEach(p -> p.accept(this));
    constructor.documentationComment().ifPresent(d -> d.accept(this));
    constructor.statements().forEach(s -> s.accept(this));
  }

  @Override
  public void visitDocumentationComment(DocumentationComment documentationComment) {
  }

  @Override
  public void visitEnum(Enum enumType) {
    visitType(enumType);
  }

  @Override
  public void visitEnumMember(EnumMember enumMember) {
    enumMember.arguments().forEach(a -> a.accept(this));
    enumMember.attributes().forEach(a -> a.accept(this));
    enumMember.documentationComment().ifPresent(d -> d.accept(this));
  }

  @Override
  public void visitEvent(Event event) {
    event.attributes().forEach(a -> a.accept(this));
    event.documentationComment().ifPresent(d -> d.accept(this));
  }

  @Override
  public void visitField(Field field) {
    field.documentationComment().ifPresent(d -> d.accept(this));
  }

  @Override
  public void visitForEach(ForEachStatement forEachStatement) {
    forEachStatement.statements().forEach(s -> s.accept(this));
  }

  @Override
  public void visitIf(IfStatement ifStatement) {
    ifStatement.sections().forEach(s -> s.accept(this));
  }

  @Override
  public void visitIfElseSection(IfElseSection ifElseSection) {
    ifElseSection.statements().forEach(s -> s.accept(this));
  }

  @Override
  public void visitInterface(Interface interfaceType) {
    visitType(interfaceType);
  }

  @Override
  public void visitInvocation(Invocation invocation) {
    invocation.arguments().forEach(i -> i.accept(this));
  }

  @Override
  public void visitMethod(Method method) {
    method.documentationComment().ifPresent(d -> d.accept(this));
    method.attributes().forEach(a -> a.accept(this));
    method.parameters().forEach(p -> p.accept(this));
    method.statements().forEach(s -> s.accept(this));
  }

  @Override
  public void visitParameter(Parameter parameter) {
    parameter.attributes().forEach(a -> a.accept(this));
  }

  @Override
  public void visitProject(Project project) {
    project.allTypes().forEach(t -> t.accept(this));
  }

  @Override
  public void visitProperty(Property property) {
    property.attributes().forEach(a -> a.accept(this));
    property.documentationComment().ifPresent(d -> d.accept(this));
  }

  @Override
  public void visitReturn(ReturnStatement returnStatement) {
  }

  @Override
  public void visitStruct(Struct structType) {
    visitType(structType);
  }

  @Override
  public void visitSwitch(SwitchStatement switchStatement) {
    switchStatement.sections().forEach(s -> s.accept(this));
  }

  @Override
  public void visitSwitchSection(SwitchSection switchSection) {
    switchSection.statements().forEach(s -> s.accept(this));
  }
}
