package com.infosupport.ldoc.reader;

import java.util.Optional;

/**
 * Interface for class of useful methods when dealing with nodes that have documentation comments.
 */
public interface HasComment {

  /**
   * Retrieve the documentation comment if it exists.
   *
   * @return Optional DocumentationComment
   */
  Optional<DocumentationComment> documentationComment();
}
