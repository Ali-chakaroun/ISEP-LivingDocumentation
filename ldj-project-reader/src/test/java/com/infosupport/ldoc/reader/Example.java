package com.infosupport.ldoc.reader;

import com.infosupport.ldoc.reader.impl.JacksonProjectFactory;
import java.io.IOException;
import java.net.URL;

public class Example {

  public static void main(String[] args) throws IOException {
    URL example = Example.class.getResource("example.json");
    Project project = new JacksonProjectFactory().project(example);

    project.classes().forEach(klass -> {
      System.out.printf("# Class %s\n\n", klass.fullName());

      klass.methods().forEach(method -> {
        System.out.printf("## Method %s\n\n", method.name());

        System.out.println(method.documentationComment().summary());
      });
    });
  }
}
