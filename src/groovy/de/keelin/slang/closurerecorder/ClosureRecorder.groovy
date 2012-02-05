package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Date: 05.02.12
 * Time: 13:04
 */
class ClosureRecorder {

  List<Expression> record(Closure closure) {
    List<Expression> sentences = []
    RecordingDelegate delegate = new RecordingDelegate(new ClosureRecording(new DelegatePropertyRegistry(), sentences))
    def originalDelegate = closure.delegate
    closure.setDelegate(delegate)
    closure()
    closure.setDelegate(originalDelegate)
    sentences
  }
}
