package com.infosupport.ldoc.analyzerj.ldjexceptions;

/**
 * General Exception that can be thrown by the Living Documentation Analyzer.
 */
public class LdocException extends RuntimeException {

  /**
   * Create a LivingDocumentation Exception with a specified message.
   */
  public LdocException(String msg) {
    super(msg);
  }

}
