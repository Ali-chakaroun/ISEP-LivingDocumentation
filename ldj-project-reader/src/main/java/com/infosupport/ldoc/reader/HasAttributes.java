package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface HasAttributes {

  Stream<Attribute> attributes();
}
