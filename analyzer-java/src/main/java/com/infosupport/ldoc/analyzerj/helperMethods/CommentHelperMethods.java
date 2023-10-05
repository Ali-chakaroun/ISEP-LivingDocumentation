package com.infosupport.ldoc.analyzerj.helperMethods;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.infosupport.ldoc.analyzerj.AnalysisVisitor;
import com.infosupport.ldoc.analyzerj.descriptions.Description;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommentHelperMethods  {

    private final AnalysisVisitor analyzer;

    public CommentHelperMethods(AnalysisVisitor analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * This method takes a comment and assign it the proper visit method.
     * and outputs it as a list.
     * @param n this is the comment that needs to be processed.
     * @return a list of Descriptions.
     */
    public Description getCommentType(Comment n) {
        Description commentType = null;
        if (n != null) {
            if (n instanceof BlockComment) {
                commentType = analyzer.visit(n.asBlockComment());
            } else if (n instanceof LineComment) {
                commentType = analyzer.visit(n.asLineComment());
            } else if (n instanceof JavadocComment) {
                commentType = analyzer.visit(n.asJavadocComment());
            }
        }
        return commentType;
    }

    public String extractSummary(JavadocComment commentText) {
        return commentText.parse().getDescription().toText().replaceAll("\r\n"," ").strip();
    }

    public  Map<String, Map<String, String>> extractParamDescriptions(JavadocComment commentText) {
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
    //keeping the logic out of main analysisvisitor class
    public void processCommentData(Map<String, Map<String, String>> commentData,
                                          StringBuilder remarks,
                                          StringBuilder returns,
                                          Map<String, String> commentParams,
                                          Map<String, String> commentTypeParams) {
        for (Map.Entry<String, Map<String, String>> entry : commentData.entrySet()) {
            String key = entry.getKey();
            Map<String, String> innerMap = entry.getValue();

            if ("RETURN".equals(key)) {
                String returnValue = innerMap.values().stream().findFirst().orElse("");
                returns.append(returnValue);
            } else if ("AUTHOR".equals(key)) {
                String authorValue = innerMap.values().stream().findFirst().orElse("");
                remarks.append(authorValue);
            } else if ("PARAM".equals(key)) {
                commentParams.putAll(innerMap);
            } else if ("TYPEPARAM".equals(key)) {
                commentTypeParams.putAll(innerMap);
            }
        }
    }
}
