package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class Util {

  public static <T> Stream<T> streamOf(JsonNode node, Function<JsonNode, T> converter) {
    return StreamSupport.stream(node.spliterator(), false).map(converter);
  }
}
