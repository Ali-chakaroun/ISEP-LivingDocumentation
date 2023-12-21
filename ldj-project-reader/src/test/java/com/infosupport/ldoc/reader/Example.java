package com.infosupport.ldoc.reader;

import com.infosupport.ldoc.reader.impl.JacksonProjectFactory;
import com.infosupport.ldoc.reader.visitor.BaseVisitor;
import java.io.IOException;
import java.net.URL;
import java.util.TreeMap;

public class Example {

  public static void main(String[] args) throws IOException {
    URL example = Example.class.getResource("example.json");
    Project project = new JacksonProjectFactory().project(example);

    project.classes().forEach(klass -> {
      System.out.printf("# Class %s\n\n", klass.fullName());

      klass.methods().forEach(method -> {
        System.out.printf("## Method %s\n\n", method.name());

        method.documentationComment().ifPresent(comment ->
          System.out.println(comment.summary()));
      });
    });

    System.out.print("# Used field types\n\n");

    TreeMap<String, Integer> counts = new TreeMap<>();
    project.accept(new BaseVisitor() {
      @Override
      public void visitField(Field field) {
        counts.merge(field.type(), 1, Integer::sum);
      }
    });
    System.out.println("| Type | Count |");
    System.out.println("| ---- | ----- |");
    counts.forEach((k, v) -> System.out.printf("| `%s` | %d |\n", k, v));
  }
}
