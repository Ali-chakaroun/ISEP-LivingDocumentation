package com.infosupport.ldoc.reader;

import java.util.List;
import java.util.stream.Stream;

public interface SwitchSection extends Node {

  List<String> labels();

  Stream<Statement> statements();

}
