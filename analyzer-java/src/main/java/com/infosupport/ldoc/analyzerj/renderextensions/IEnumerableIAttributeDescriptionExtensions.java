package com.infosupport.ldoc.analyzerj.renderextensions;

import com.infosupport.ldoc.analyzerj.descriptions.AttributeDescription;
import java.util.List;

public class IEnumerableIAttributeDescriptionExtensions {

  /**
   * Filter a list of {@link AttributeDescription}s on the given type.
   *
   * @param list           List of {@link AttributeDescription}.
   * @param fullNameOfType String of the type, including packages (thus fullName).
   * @return List of {@link AttributeDescription} with the given type.
   */
  public static List<AttributeDescription> attributesOfType(List<AttributeDescription> list,
      String fullNameOfType) {
    if (list == null) {
      throw new IllegalArgumentException("AttributeDescription List cannot be null");
    }

    return list.stream().filter(attribute -> attribute.type().equals(fullNameOfType)).toList();
  }

  /**
   * Check if a list of {@link AttributeDescription}s contains at least one attribute with the given
   * type.
   *
   * @param list           List of {@link AttributeDescription}.
   * @param fullNameOfType String of the type, including packages (thus fullName).
   * @return True if at least one attribute has the given type, false if not.
   */
  public static boolean hasAttribute(List<AttributeDescription> list, String fullNameOfType) {
    return list.stream().anyMatch(attribute -> attribute.type().equals(fullNameOfType));
  }
}
