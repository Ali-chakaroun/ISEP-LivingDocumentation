package com.infosupport.ldoc.reader;

import java.util.Optional;

public interface HasComment {

  Optional<DocumentationComment> documentationComment();
}
