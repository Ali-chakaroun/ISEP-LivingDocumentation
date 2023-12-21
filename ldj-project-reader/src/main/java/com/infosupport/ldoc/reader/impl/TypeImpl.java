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
import java.util.stream.Stream;

class TypeImpl {

  private final ProjectImpl project;
  private final JsonNode node;

  TypeImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  public String fullName() {
    return node.path("FullName").textValue();
  }

  public String name() {
    String fullName = fullName();
    return fullName.substring(fullName.lastIndexOf('.') + 1);
  }

  public List<String> basetypes() {
    return project.objectMapper().convertValue(node.path("Basetypes"), new TypeReference<>() {
    });
  }

  public Stream<Field> fields() {
    return Util.streamOf(node.path("Fields"), FieldImpl::new);
  }

  public Stream<Constructor> constructors() {
    return Util.streamOf(node.path("Constructors"), c -> new ConstructorImpl(project, c));
  }

  public Stream<Method> methods() {
    return Util.streamOf(node.path("Methods"), m -> new MethodImpl(project, m));
  }

  public Stream<Property> properties() {
    return Util.streamOf(node.path("Properties"), p -> new PropertyImpl(project, p));
  }

  public Stream<Attribute> attributes() {
    return Util.streamOf(node.path("Attributes"), AttributeImpl::new);
  }

  public Stream<EnumMember> enumMembers() {
    return Util.streamOf(node.path("EnumMembers"), m -> new EnumMemberImpl(project, m));
  }

  public Stream<Event> events() {
    return Util.streamOf(node.path("Events"), e -> new EventImpl(project, e));
  }

  public DocumentationComment documentationComment() {
    return new DocumentationCommentImpl(project, node.path("DocumentationComments"));
  }
}
