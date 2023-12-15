package com.infosupport.ldoc.reader.impl;

import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.Constructor;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.EnumMember;
import com.infosupport.ldoc.reader.Event;
import com.infosupport.ldoc.reader.Field;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Property;
import com.infosupport.ldoc.reader.Type;
import com.infosupport.ldoc.reader.Visitor;
import java.util.List;
import java.util.stream.Stream;

class UnknownTypeImpl implements Type {

  @Override
  public void accept(Visitor v) {
    /* Do nothing. */
  }

  @Override
  public String fullName() {
    return null;
  }

  @Override
  public List<String> basetypes() {
    return List.of();
  }

  @Override
  public Stream<Field> fields() {
    return Stream.empty();
  }

  @Override
  public Stream<Constructor> constructors() {
    return Stream.empty();
  }

  @Override
  public Stream<Method> methods() {
    return Stream.empty();
  }

  @Override
  public Stream<Property> properties() {
    return Stream.empty();
  }

  @Override
  public Stream<Attribute> attributes() {
    return Stream.empty();
  }

  @Override
  public Stream<EnumMember> enumMembers() {
    return Stream.empty();
  }

  @Override
  public Stream<Event> events() {
    return Stream.empty();
  }

  @Override
  public DocumentationComment documentationComment() {
    return null;
  }
}
