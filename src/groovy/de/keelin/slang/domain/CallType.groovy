package de.keelin.slang.domain

/**
 * Describes the syntactical type of a {@link Call}.
 *
 * @author Markus Günther
 */
enum CallType {
  /**
   * a method call
   */
  METHOD,
  /**
   * any reading access to a property
   * either on the {@link CallOrigin#DELEGATE}
   * or the {@link CallOrigin#PREDECESSOR}
   */
  PROPERTY_READ,
  /**
   * Not yet implemented
   */
  PROPERTY_ASSIGNMENT,
  /**
   * any pogo
   */
  OBJECT_REF,
  /**
   * key-value pair in a Map (method parameter)
   */
  MAP_ENTRY
}
