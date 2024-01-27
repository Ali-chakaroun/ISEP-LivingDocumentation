package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Argument;
import com.infosupport.ldoc.reader.HasType;
import com.infosupport.ldoc.reader.Invocation;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Visitor;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

class InvocationImpl implements Invocation {

  private final ProjectImpl project;
  private final JsonNode node;

  InvocationImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String containingType() {
    return node.path("ContainingType").textValue();
  }

  @Override
  public String name() {
    return node.path("Name").textValue();
  }

  @Override
  public Stream<Argument> arguments() {
    return Util.streamOf(node.path("Arguments"), ArgumentImpl::new);
  }

  @Override
  public Optional<Method> getInvokedMethod() {
    return project
        .type(containingType())
        .flatMap(type -> type.methodsWithName(name())
            .filter(method -> Arrays.equals(
                method.parameters().map(HasType::type).toArray(),
                arguments().map(HasType::type).toArray()))
            .findFirst());
  }

  @Override
  public void accept(Visitor v) {
    v.visitInvocation(this);
  }
}
