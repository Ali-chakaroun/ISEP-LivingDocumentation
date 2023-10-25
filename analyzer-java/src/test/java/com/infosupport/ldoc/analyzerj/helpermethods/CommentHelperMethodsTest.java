package com.infosupport.ldoc.analyzerj.helpermethods;

import static com.infosupport.ldoc.analyzerj.helpermethods.CommentHelperMethods.extractParamDescriptions;
import static com.infosupport.ldoc.analyzerj.helpermethods.CommentHelperMethods.extractSummary;
import static com.infosupport.ldoc.analyzerj.helpermethods.CommentHelperMethods.processCommentData;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.JavadocBlockTag.Type;
import com.github.javaparser.javadoc.description.JavadocDescription;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CommentHelperMethodsTest {

  @Test
  void extract_summary_test() {
    JavadocComment javadocComment = new JavadocComment("This is a remark.");
    assertEquals("This is a remark.", extractSummary(javadocComment));
  }

  @Test
  void extract_param_description_test() {

    JavadocBlockTag paramBlockTag1 = new JavadocBlockTag(Type.PARAM,
        "param1 Description of param1.");
    JavadocBlockTag paramBlockTag2 = new JavadocBlockTag(Type.PARAM,
        "param2 Description of param2.");
    JavadocBlockTag paramBlockTag3 = new JavadocBlockTag(Type.PARAM,
        "L<param3> list of param3.");
    JavadocDescription javadocDescription = new JavadocDescription();
    Javadoc javadoc = new Javadoc(javadocDescription);
    javadoc.addBlockTag(paramBlockTag1);
    javadoc.addBlockTag(paramBlockTag2);
    javadoc.addBlockTag(paramBlockTag3);
    Map<String, Map<String, String>> paramDescriptions = extractParamDescriptions(
        javadoc.toComment());
    assertEquals("Description of param1.", paramDescriptions.get("PARAM").get("param1"));
    assertEquals("Description of param2.", paramDescriptions.get("PARAM").get("param2"));
    assertEquals("list of param3.", paramDescriptions.get("PARAM").get("L<param3>"));
  }

  @Test
  void process_comment_data_test() {
    Map<String, Map<String, String>> commentData = new HashMap<>();
    Map<String, String> paramData = new HashMap<>();
    paramData.put("param1", "Description of param1.");
    paramData.put("param2", "Description of param2.");
    paramData.put("<L<param3>>", "list of param3.");
    commentData.put("PARAM", paramData);
    commentData.put("RETURN", Map.of("", "return a string."));
    StringBuilder returns = new StringBuilder();
    Map<String, String> commentParams = new LinkedHashMap<>();
    Map<String, String> commentTypeParams = new LinkedHashMap<>();
    processCommentData(commentData, returns, commentParams, commentTypeParams);
    assertEquals("Description of param1.", commentParams.get("param1"));
    assertEquals("Description of param2.", commentParams.get("param2"));
    assertEquals("list of param3.", commentTypeParams.get("L<param3>"));
    assertEquals("return a string.", returns.toString());
  }

}
