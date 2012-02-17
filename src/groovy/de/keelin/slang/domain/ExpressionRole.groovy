package de.keelin.slang.domain

/**
 * Describes the role of an Expression within it's sentence.
 *
 * @author Markus Günther
 */
enum ExpressionRole {
  /**
   * the expression that encloses the whole sentence
   */
  SENTENCE_ROOT,
  /**
   * any Expression that has a method-Call as its parent
   */
  METHOD_PARAM,
  /**
   * any Expression used as value in a Map entry
   */
  MAP_VALUE,
  /**
   * Not yet implemented
   */
  ASSIGNMENT_VALUE
}
