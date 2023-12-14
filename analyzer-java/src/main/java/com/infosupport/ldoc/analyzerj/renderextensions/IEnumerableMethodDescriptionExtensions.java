package com.infosupport.ldoc.analyzerj.renderextensions;

import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import java.util.List;

public class IEnumerableMethodDescriptionExtensions {


  /**
   * Filter a list of {@link MethodDescription} to find all methods with the given name.
   *
   * @param list List of {@link MethodDescription}
   * @param name String with method name.
   * @return List of all {@link MethodDescription}s that have the given name.
   */
  public static List<MethodDescription> methodsWithName(List<MethodDescription> list,
      String name) {
    if (list == null) {
      throw new IllegalArgumentException("MethodDescription List cannot be null");
    }
    return list.stream().filter(m -> m.member().name().equals(name)).toList();
  }

}
