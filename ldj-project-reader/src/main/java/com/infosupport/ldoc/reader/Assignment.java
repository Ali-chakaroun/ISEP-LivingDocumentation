package com.infosupport.ldoc.reader;

public interface Assignment extends Statement {

  String left();

  String operator();

  String right();
}
