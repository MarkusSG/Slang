package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Date: 01.02.12
 * Time: 11:20
 */
class DelegatePropertyRegistry {

  Map<RecordingDelegate, Expression> recordings2Expressions = [:]
  List<Expression> expressions = []

  def add(Expression expression, RecordingDelegate delegate) {
    recordings2Expressions[(delegate)] = expression
    expressions << expression
  }

  Expression getExpression(RecordingDelegate delegate) {
    recordings2Expressions[(delegate)]
  }

  def remove(RecordingDelegate delegate) {
    def removeExpression = recordings2Expressions.remove(delegate)
    expressions.remove(removeExpression)
    removeExpression
  }
}
