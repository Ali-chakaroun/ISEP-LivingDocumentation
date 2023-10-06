package com.infosupport.ldoc.analyzerj.helperMethods;

import com.github.javaparser.ast.comments.JavadocComment;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommentHelperMethods {

  public static String extractSummary(JavadocComment commentText) {
    return commentText.parse().getDescription().toText().strip();
  }

  public static Map<String, Map<String, String>> extractParamDescriptions(JavadocComment commentText) {
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

  // Keeping this logic out of main analysisvisitor class.
  public static void processCommentData(Map<String, Map<String, String>> commentData,
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
        commentParams.putAll(innerMap);
      } else if ("TYPEPARAM".equals(key)) {
        commentTypeParams.putAll(innerMap);
      }
    }
  }
}
