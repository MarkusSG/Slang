package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Date: 02.02.12
 * Time: 16:52
 */
class ClosureRecording {

  final DelegatePropertyRegistry registry

  ClosureRecording(final DelegatePropertyRegistry registry) {
    this.registry = registry
  }

  def recordPropertyRead(final String name) {
    Expression expression = Expression.sentenceRoot()
    ExpressionRecording recording = new ExpressionRecording(expression, registry)
    recording.recordPropertyRead(name)
    ExpressionRecordingDelegate result = new ExpressionRecordingDelegate(recording)
    registry.add(expression, result)
    result
  }

}
