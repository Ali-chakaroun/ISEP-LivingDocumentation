package com.infosupport.ldoc.analyzerj.helperMethods;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.infosupport.ldoc.analyzerj.AnalysisVisitor;
import com.infosupport.ldoc.analyzerj.Analyzer;
import com.infosupport.ldoc.analyzerj.descriptions.Description;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentHelperMethods  {

    private final AnalysisVisitor analyzer;

    public CommentHelperMethods(AnalysisVisitor analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * This method takes a comment and assign it the proper visit method.
     * @param n this is the comment that needs to be processed.
     * @param arg this is the analyzer.
     * @return a list of Descriptions.
     */
    public List<Description> getCommentType(Comment n, Analyzer arg) {
        List<Description> commentType = new ArrayList<>();
        if (n != null) {
            if (n instanceof BlockComment) {
                commentType.addAll(arg != null ? analyzer.visit(n.asBlockComment(), arg) : analyzer.visit(n.asBlockComment()));
            } else if (n instanceof LineComment) {
                commentType.addAll(arg != null ? analyzer.visit(n.asLineComment(), arg) : analyzer.visit(n.asLineComment()));
            } else if (n instanceof JavadocComment) {
                commentType.addAll(arg != null ? analyzer.visit(n.asJavadocComment(), arg) : analyzer.visit(n.asJavadocComment()));
            }
        }
        return commentType;
    }
    public String cleanComment(String commentText) {
        // Split the comment into lines
        String[] lines = commentText.split("\\r?\\n");
        StringBuilder cleanedComment = new StringBuilder();

        for (String line : lines) {
            // Remove leading "*" characters after "/" or "\r\n"
            String cleanedLine = line.replaceFirst("^(?:/|\\s*\\*)+", "");
            cleanedComment.append(cleanedLine);
        }

        return cleanedComment.toString().trim();
    }

    public String extractSummary(String commentText) {
        // Use a regular expression to match everything before the first @
        Pattern summaryPattern = Pattern.compile("^(.*?)@", Pattern.DOTALL);
        Matcher matcher = summaryPattern.matcher(commentText);
        if (matcher.find()) {
            return matcher.group(1).trim(); // Trim to remove leading/trailing spaces
        }
        return "";
    }

    public Map<String, String> extractParamDescriptions(String commentText) {
        Map<String, String> paramDescriptions = new LinkedHashMap<>();
        // Use a regular expression to match @param lines
        Pattern paramPattern = Pattern.compile("@param\\s+(\\w+)\\s+(.*)");
        Matcher matcher = paramPattern.matcher(commentText);

        while (matcher.find()) {
            String paramName = matcher.group(1);
            String paramDescription = matcher.group(2);

            paramDescriptions.put(paramName, paramDescription);
        }
        return paramDescriptions;
    }
}
