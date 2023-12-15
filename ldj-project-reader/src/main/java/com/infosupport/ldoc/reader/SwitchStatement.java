package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface SwitchStatement extends Statement {

  String expression();

  Stream<SwitchSection> sections();

}
