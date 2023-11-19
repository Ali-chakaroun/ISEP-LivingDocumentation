package com.infosupport.ldoc.springexample;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class SpringRendererTest {

  @Test
  void constructor() {
    assertDoesNotThrow(SpringRenderer::new);
  }
}
