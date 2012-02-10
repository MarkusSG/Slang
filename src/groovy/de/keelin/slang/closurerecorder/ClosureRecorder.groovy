package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * A helper class for recording all external calls
 * method calls and property accesses) made from
 * within a closure.
 */
class ClosureRecorder {

  /**
   * Records all method calls and property accesses
   * the specified closure attempts against the delegate.
   * For Example:
   * <p/>
   * <pre>
   * record({
   *   the quick, brown fox
   *   jumps over: the lazy dog
   * })
   * </pre>
   * will return a List containing two Expressions,
   * one for the first line ("the quick, brown fox") and one
   * for the second.
   * <p/>
   * ATTENTION:
   * <ol><li>the closure will be executed against a different
   * delegate for recording purposes,
   * so side effects may occur. </li>
   * <li>only executed parts of the closure will be recorded;
   * skipped if-else-branches will not</li><ol/>
   *
   * @param closure the closure to be recorded
   * @return a List of Expressions, one for each
   * "sentence" in the recorded closure
   * @see Expression
   *
   * @author Markus Günther
   */
  List<Expression> record(Closure closure) {
    List<Expression> sentences = []
    def registry = new DelegatePropertyRegistry()
    RecordingDelegate delegate = new RecordingDelegate(new ClosureRecording(registry, sentences))
    def originalDelegate = closure.delegate
    closure.setDelegate(delegate)
    closure()
    // add all unused Expressions from the registry as sentences
    sentences.addAll(registry.expressions)
    closure.setDelegate(originalDelegate)
    sentences
  }
}
