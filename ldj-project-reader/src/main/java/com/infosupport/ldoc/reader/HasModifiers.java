package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with nodes that have modifiers.
 */
public interface HasModifiers {

  /**
   * Retrieve modifiers.
   *
   * @return modifier long.
   */
  long modifiers();

  /**
   * Check if node has a given modifier.
   *
   * @param modifier Modifier to check for.
   * @return true if node has given modifier, false if not.
   */
  default boolean hasModifier(Modifier modifier) {
    return (modifiers() & modifier.mask()) != 0;
  }

  /**
   * Check if node has public modifier.
   *
   * @return true if node has public modifier, false if not.
   */
  default boolean isPublic() {
    return hasModifier(Modifier.PUBLIC);
  }

  /**
   * Check if node has private modifier.
   *
   * @return true if node has private modifier, false if not.
   */
  default boolean isPrivate() {
    return hasModifier(Modifier.PRIVATE);
  }

  /**
   * Check if node has protected modifier.
   *
   * @return true if node has protected modifier, false if not.
   */
  default boolean isProtected() {
    return hasModifier(Modifier.PROTECTED);
  }

  /**
   * Check if node has static modifier.
   *
   * @return true if node has static modifier, false if not.
   */
  default boolean isStatic() {
    return hasModifier(Modifier.STATIC);
  }
}
