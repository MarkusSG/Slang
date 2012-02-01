package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Date: 01.02.12
 * Time: 11:20
 */
class DelegatePropertyRegistry {

  Map<ExpressionRecording, Expression> recordings2Expressions = [:]
  List<Expression> expressions = []

  def add(Expression expression, ExpressionRecording recording) {
    recordings2Expressions[(recording)] = expression
    expressions << expression
  }

  Expression getExpression(ExpressionRecording recording) {
    recordings2Expressions[(recording)]
  }

  def remove(ExpressionRecording recording) {
    def removeExpression = recordings2Expressions.remove(recording)
    expressions.remove(removeExpression)
    removeExpression
  }
}
