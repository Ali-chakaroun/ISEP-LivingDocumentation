package com.infosupport.ldoc.springexample;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class SpringEventRendererTest {

  @Test
  void constructor() {
    assertDoesNotThrow(SpringEventRenderer::new);
  }
}
