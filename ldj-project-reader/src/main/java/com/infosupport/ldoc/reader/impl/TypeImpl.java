package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.Constructor;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.EnumMember;
import com.infosupport.ldoc.reader.Event;
import com.infosupport.ldoc.reader.Field;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Property;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class TypeImpl {

  private final ProjectImpl project;
  private final JsonNode node;

  TypeImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  private <T> Stream<T> streamOf(String path, Function<JsonNode, T> converter) {
    return StreamSupport.stream(node.path(path).spliterator(), false).map(converter);
  }

  public String fullName() {
    return node.path("FullName").textValue();
  }

  public List<String> basetypes() {
    return project.objectMapper().convertValue(node.path("Basetypes"), new TypeReference<>() {
    });
  }

  public Stream<Field> fields() {
    return streamOf("Fields", f -> new FieldImpl(project, f));
  }

  public Stream<Constructor> constructors() {
    return streamOf("Constructors", c -> new ConstructorImpl(project, c));
  }

  public Stream<Method> methods() {
    return streamOf("Methods", m -> new MethodImpl(project, m));
  }

  public Stream<Property> properties() {
    return streamOf("Properties", p -> new PropertyImpl(project, p));
  }

  public Stream<Attribute> attributes() {
    return streamOf("Attributes", a -> new AttributeImpl(project, a));
  }

  public Stream<EnumMember> enumMembers() {
    return streamOf("EnumMembers", m -> null);
  }

  public Stream<Event> events() {
    return streamOf("Events", e -> null);
  }

  public DocumentationComment documentationComment() {
    return new DocumentationCommentImpl(project, node.get("DocumentationComments"));
  }
}
