package com.infosupport.ldoc.analyzerj.renderextensions;

import java.util.List;

public class IEnumerableStringExtensions {

  /**
   * Filter a list of strings on whether they start with the given partial name.
   *
   * @param list        List of strings.
   * @param partialName String of the partial name.
   * @return List of the strings that start with partialName.
   */
  public static List<String> startsWith(List<String> list, String partialName) {
    if (list == null) {
      throw new IllegalArgumentException("String List cannot be null");
    }
    if (partialName == null) {
      throw new IllegalArgumentException("Partial name cannot be null");
    }

    return list.stream().filter(bt -> bt.startsWith(partialName)).toList();
  }
}
