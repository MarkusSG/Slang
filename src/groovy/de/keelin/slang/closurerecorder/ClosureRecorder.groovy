package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Date: 05.02.12
 * Time: 13:04
 */
class ClosureRecorder {

  List<Expression> record(Closure closure) {
    List<Expression> sentences = []
    def registry = new DelegatePropertyRegistry()
    RecordingDelegate delegate = new RecordingDelegate(new ClosureRecording(registry, sentences))
    def originalDelegate = closure.delegate
    closure.setDelegate(delegate)
    closure()
    sentences.addAll(registry.expressions)
    closure.setDelegate(originalDelegate)
    sentences
  }
}
