package com.infosupport.ldoc.reader;

public interface Modified {

  long modifiers();

  default boolean hasModifier(Modifier modifier) {
    return (modifiers() & modifier.mask()) != 0;
  }

  default boolean isPublic() {
    return hasModifier(Modifier.PUBLIC);
  }

  default boolean isPrivate() {
    return hasModifier(Modifier.PRIVATE);
  }

  default boolean isProtected() {
    return hasModifier(Modifier.PROTECTED);
  }

  default boolean isStatic() {
    return hasModifier(Modifier.STATIC);
  }
}
