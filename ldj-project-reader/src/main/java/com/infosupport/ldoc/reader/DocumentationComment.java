package com.infosupport.ldoc.reader;

import java.util.Map;

public interface DocumentationComment extends Node {
  String example();

  String remarks();

  String returns();

  String summary();

  String value();

  Map<String, String> exceptions();

  Map<String, String> permissions();

  Map<String, String> seeAlsos();

  Map<String, String> typeParams();

  Map<String, String> params();
}
