package com.infosupport.ldoc.reader;

import java.util.Map;

/**
 * Interface for class of useful methods when dealing with DocumentationComment nodes.
 */
public interface DocumentationComment extends Node {

  /**
   * Retrieve example.
   *
   * @return String containing the example.
   */
  String example();

  /**
   * Retrieve remarks.
   *
   * @return String containing the remarks.
   */
  String remarks();

  /**
   * Retrieve documentation on return value.
   *
   * @return String containing the returns info.
   */
  String returns();

  /**
   * Retrieve summary.
   *
   * @return String containing the summary.
   */
  String summary();

  /**
   * Retrieve value.
   *
   * @return String containing the value.
   */
  String value();

  /**
   * Retrieve exceptions.
   *
   * @return String Map containing the exceptions.
   */
  Map<String, String> exceptions();

  /**
   * Retrieve permissions.
   *
   * @return String Map containing the permissions.
   */
  Map<String, String> permissions();

  /**
   * Retrieve seeAlsos.
   *
   * @return String Map containing the seeAlsos.
   */
  Map<String, String> seeAlsos();

  /**
   * Retrieve type parameters.
   *
   * @return String Map containing the type parameters.
   */
  Map<String, String> typeParams();

  /**
   * Retrieve parameters.
   *
   * @return String Map containing the parameters.
   */
  Map<String, String> params();
}
