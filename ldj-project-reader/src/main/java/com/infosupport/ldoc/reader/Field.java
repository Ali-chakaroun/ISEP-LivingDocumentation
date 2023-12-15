package com.infosupport.ldoc.reader;

public interface Field extends Node {

  String name();

  String type();

  String initializer();

}
