package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Date: 02.02.12
 * Time: 16:52
 */
class ClosureRecording {

  final DelegatePropertyRegistry registry
  final List<Expression> sentences

  ClosureRecording(final DelegatePropertyRegistry registry) {
    this(registry, [])
  }

  ClosureRecording(final DelegatePropertyRegistry registry, final List<Expression> sentences) {
    this.registry = registry
    this.sentences = sentences
  }

  def recordPropertyRead(final String name) {
    Expression expression = Expression.sentenceRoot()
    ExpressionRecording recording = new ExpressionRecording(expression, registry)
    recording.recordPropertyRead(name)
    ExpressionRecordingDelegate result = new ExpressionRecordingDelegate(recording)
    registry.add(expression, result)
    result
  }

  def recordMethodCall(final String name, final List args) {
    Expression expression = Expression.sentenceRoot()
    sentences << expression
    ExpressionRecording recording = new ExpressionRecording(expression, registry)
    recording.recordMethodCall(name, args)
    ExpressionRecordingDelegate result = new ExpressionRecordingDelegate(recording)
    result
  }

}
