package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Attributed {

  Stream<Attribute> attributes();
}
