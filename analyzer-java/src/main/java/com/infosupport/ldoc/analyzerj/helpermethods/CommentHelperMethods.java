package com.infosupport.ldoc.analyzerj.helpermethods;

import com.github.javaparser.ast.comments.JavadocComment;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CommentHelperMethods is a utility class with methods related to JavadocComment handling.
 */
public final class CommentHelperMethods {

  private CommentHelperMethods() {
    // Private constructor to prevent instantiation
  }

  /**
   * Returns the description of the given Javadoc comment, as text.
   */
  public static String extractSummary(JavadocComment commentText) {
    return commentText.parse().getDescription().toText().strip();
  }

  /**
   * Extracts the contents of block tags like <code>@param</code> as strings. The resulting map has
   * keys like "PARAM" (for <code>@param</code>) and "RETURN" (for <code>@return</code>) at the top
   * level, and then the names and descriptions for each parameter. If there are no block tags, the
   * resulting map is empty.
   */
  public static Map<String, Map<String, String>> extractParamDescriptions(
      JavadocComment commentText) {
    Map<String, Map<String, String>> paramDescriptions = new LinkedHashMap<>();
    for (var results : commentText.parse().getBlockTags()) {
      String tagType = results.getType().toString();
      String paramName = results.getName().orElse("placeHolder");
      String content = results.getContent().toText();
      if (!paramDescriptions.containsKey(tagType)) {
        paramDescriptions.put(tagType, new LinkedHashMap<>());
      }
      paramDescriptions.get(tagType).put(paramName, content);
    }
    return paramDescriptions;
  }

  /**
   * Separate the given comment into the shape expected by <code>CommentSummaryDescription</code>.
   *
   * @param commentData       a parameter description from <code>extractParamDescriptions</code>
   * @param returns           if a return value is documented, it is appended here
   * @param commentParams     if parameters are documented, this map is mutated to include them
   * @param commentTypeParams if type parameters are documented, this map is mutated to add them
   */
  public static void processCommentData(
      Map<String, Map<String, String>> commentData,
      StringBuilder returns,
      Map<String, String> commentParams,
      Map<String, String> commentTypeParams) {
    for (Map.Entry<String, Map<String, String>> entry : commentData.entrySet()) {
      String key = entry.getKey();
      Map<String, String> innerMap = entry.getValue();
      if ("RETURN".equals(key)) {
        String returnValue = innerMap.values().stream().findFirst().orElse("");
        returns.append(returnValue);
      } else if ("PARAM".equals(key)) {
        for (Map.Entry<String, String> paramEntry : innerMap.entrySet()) {
          String paramKey = paramEntry.getKey();
          String paramValue = paramEntry.getValue();
          // Check if the key is enclosed in angle brackets
          if (isTypeParam(paramKey)) {
            commentTypeParams.put(extractInnerValues(paramKey), paramValue);
          } else {
            commentParams.put(paramKey, paramValue);
          }
        }
      }
    }
  }

  private static Boolean isTypeParam(String input) {
    return input.length() > 2 && input.startsWith("<") && input.endsWith(">");
  }

  private static String extractInnerValues(String input) {
    // Return the content inside angle brackets
    return input.substring(1, input.length() - 1);
  }
}
