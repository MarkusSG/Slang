package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * registry for property accesses on the closure's delegate.
 * These must be handled in a special way, because they may
 * either be used as parameters for method calls or become
 * sentences in their own right.
 *
 * @author Markus Günther
 */
protected class DelegatePropertyRegistry {

  /*
   *  handles the relationship between recordingDelegates
   *  and Expressions. Necessary for
   *  {@link #getExpression(RecordingDelegate)).
   */
  Map<RecordingDelegate, Expression> recordings2Expressions = [:]
  /*
   * Is used to make sure the creation order of the stored
   * Expressions stays intact.
   */
  List<Expression> expressions = []

  /**
   * adds one Expression to the registry.
   * @param expression the expression
   * @param delegate the expressions RecordingDelegate
   */
  def add(Expression expression, RecordingDelegate delegate) {
    expressions << expression
    recordings2Expressions[(delegate)] = expression
  }

  /**
   * Retrieves the Expression handled by the specified RecordingDelegate.
   * @param delegate the delegate for which the Expression shall be retrieved
   * @return the Expression handled by the RecordingDelegate
   */
  Expression getExpression(RecordingDelegate delegate) {
    recordings2Expressions[(delegate)]
  }

  /**
   * Removes the Expression handled by the specified RecordingDelegate.
   * @param delegate the delegate for which the Expression shall be removed
   * @return the Expression handled by the RecordingDelegate
   */
  def remove(RecordingDelegate delegate) {
    def removeExpression = recordings2Expressions.remove(delegate)
    expressions.remove(removeExpression)
    removeExpression
  }
}
