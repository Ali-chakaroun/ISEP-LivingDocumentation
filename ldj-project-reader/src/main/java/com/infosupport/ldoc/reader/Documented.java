package com.infosupport.ldoc.reader;

import java.util.Optional;

public interface Documented {

  Optional<DocumentationComment> documentationComment();
}
