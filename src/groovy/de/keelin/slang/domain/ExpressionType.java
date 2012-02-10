package de.keelin.slang.domain;

/**
 * Describes the syntactical type of an {@link Expression}.
 *
 * @author Markus Günther
 */
public enum ExpressionType {
  /**
   * a chain of {@link Call}s
   */
  CALL_CHAIN,
  /**
   * a Map (probably used as method parameter
   */
  MAP
}
