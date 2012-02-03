package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Date: 02.02.12
 * Time: 16:52
 */
class ClosureRecording implements Recording{

  final DelegatePropertyRegistry registry
  final List<Expression> sentences

  ClosureRecording(final DelegatePropertyRegistry registry) {
    this(registry, [])
  }

  ClosureRecording(final DelegatePropertyRegistry registry, final List<Expression> sentences) {
    this.registry = registry
    this.sentences = sentences
  }

  RecordingDelegate recordPropertyRead(final String name) {
    Expression expression = Expression.sentenceRoot()
    ExpressionRecording recording = new ExpressionRecording(expression, registry)
    recording.recordPropertyRead(name)
    RecordingDelegate result = new RecordingDelegate(recording)
    registry.add(expression, result)
    result
  }

  RecordingDelegate recordMethodCall(final String name, final List args) {
    Expression expression = Expression.sentenceRoot()
    ExpressionRecording recording = new ExpressionRecording(expression, registry)
    recording.recordMethodCall(name, args)
    sentences.addAll(registry.expressions)
    registry.expressions.clear()
    sentences << expression
    RecordingDelegate result = new RecordingDelegate(recording)
    result
  }

}
