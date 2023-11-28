package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record TypeDescription(
    @JsonProperty("Type")
    @JsonInclude(value = Include.CUSTOM, valueFilter = TypeTypeFilter.class)
    @JsonFormat(shape = Shape.NUMBER)
    TypeType type,

    @JsonProperty("Modifiers")
    @JsonInclude(Include.NON_DEFAULT)
    int modifiers,

    @JsonProperty("FullName")
    String fullName,

    @JsonProperty("BaseTypes")
    @JsonInclude(Include.NON_EMPTY)
    List<String> baseTypes,

    @JsonProperty("DocumentationComments")
    @JsonInclude(Include.NON_NULL)
    Description comments,

    @JsonProperty("Fields")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> fields,

    @JsonProperty("Constructors")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> constructors,

    @JsonProperty("Methods")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> methods,

    @JsonProperty("Attributes")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> attributes,

    @JsonProperty("EnumMembers")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> enumMembers)

    implements Description {

  /** Builder class for constructing TypeDescriptions incrementally. */
  public static class Builder {

    private final TypeType type;
    private int modifiers = Modifier.NONE.mask();
    private final String fullName;
    private final List<String> baseTypes = new ArrayList<>();
    private Description comment = null;
    private final List<Description> fields = new ArrayList<>();
    private final List<Description> constructors = new ArrayList<>();
    private final List<Description> methods = new ArrayList<>();
    private final List<Description> attributes = new ArrayList<>();
    private final List<Description> enumMembers = new ArrayList<>();

    /** Start building a new TypeDescription with this TypeType and fully qualified name. */
    public Builder(TypeType type, String fullName) {
      this.type = type;
      this.fullName = fullName;
    }

    /** Changes this Builder so that every product also has the given fully qualified base types. */
    public Builder withBaseTypes(List<String> baseTypes) {
      this.baseTypes.addAll(baseTypes);
      return this;
    }

    /** Changes this Builder so that every product built also has the given modifiers. */
    public Builder withModifiers(int modifiers) {
      this.modifiers = this.modifiers | modifiers;
      return this;
    }

    /** Changes this Builder so that the given comment is attached to every product. */
    public Builder withComment(Description comment) {
      this.comment = comment;
      return this;
    }

    /** Changes this Builder so that the given attributes (annotations) are added to the product. */
    public Builder withAttributes(List<Description> attributes) {
      this.attributes.addAll(attributes);
      return this;
    }

    /** Changes this Builder so the given members are added to the product. */
    public Builder withMembers(Description... members) {
      return withMembers(List.of(members));
    }

    /** Changes this Builder so the members in the given list are added to the product. */
    public Builder withMembers(List<Description> members) {
      for (Description member : members) {
        if (member instanceof ConstructorDescription) {
          this.constructors.add(member);
        } else if (member instanceof MethodDescription) {
          this.methods.add(member);
        } else if (member instanceof FieldDescription) {
          this.fields.add(member);
        } else if (member instanceof EnumMemberDescription) {
          this.enumMembers.add(member);
        }
      }
      return this;
    }

    /** Produces an immutable product TypeDescription. */
    public TypeDescription build() {
      return new TypeDescription(
          type,
          modifiers,
          fullName,
          Collections.unmodifiableList(baseTypes),
          comment,
          Collections.unmodifiableList(fields),
          Collections.unmodifiableList(constructors),
          Collections.unmodifiableList(methods),
          Collections.unmodifiableList(attributes),
          Collections.unmodifiableList(enumMembers));
    }
  }

  public TypeDescription(TypeType type, String fullName, List<String> baseTypes) {
    this(
        type,
        Modifier.NONE.mask(),
        fullName,
        baseTypes,
        null,
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }
}
