package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Description containing name, modifiers and annotations (attributes in .NET terms) for use by
 * other descriptions.
 *
 * @param name       Name of the member (string).
 * @param modifiers  {@link Modifier}s of the member.
 * @param attributes List of annotations ({@link AttributeDescription}).
 * @param comments     JavaDoc comments in an {@link DocumentationCommentsDescription}.
 */
public record MemberDescription(
    @JsonProperty("Name")
    String name,

    @JsonProperty("Modifiers")
    @JsonInclude(Include.NON_DEFAULT)
    int modifiers,

    @JsonProperty("Attributes")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> attributes,

    @JsonProperty("DocumentationComments")
    @JsonInclude(Include.NON_NULL)
    Description comments
) implements Description {

  /**
   * Constructor that takes only a name. The modifiers value is set to {@link Modifier#NONE}, list
   * of attributes is empty.
   *
   * @param name Name of the member (string).
   */
  public MemberDescription(String name) {
    this(name, 0, List.of(), null);
  }
}
