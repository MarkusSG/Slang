package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression

/**
 * Recording class for a whole closure.
 * the record...() methods of this class returns instances of
 * {@link RecordingDelegate), which should be returned to the
 * original caller of the recorded method / property, so
 * further calls on these can be told from top-level ones.
 *
 * @see ClosureRecorder
 * @see RecordingDelegate
 *
 * @author Markus Günther
 */
protected class ClosureRecording implements Recording{

  /**
   * here top-level property accesses will be stored until
   * their role (top-level sentence or parameter for a
   * call within another expression) can be determined
   */
  final DelegatePropertyRegistry registry

  /**
   * here all sentences will be stored.
   */
  final List<Expression> sentences

  /**
   * Creates a new instance which will use the specified
   * {@link DelegatePropertyRegistry}.
   * @param registry
   */
  ClosureRecording(final DelegatePropertyRegistry registry) {
    this(registry, [])
  }

  ClosureRecording(final DelegatePropertyRegistry registry, final List<Expression> sentences) {
    this.registry = registry
    this.sentences = sentences
  }

  /**
   * Records the specified property access.
   * This call will be registered by in the
   * DelegatePropertyRegistry, but not (yet)
   * as a sentence, because it's not yet
   * clear if it is a sentence or a just
   * method parameter or an assignment value.
   * @param name the name of the property
   * @return the recording delegate for the
   * {@link Expression} created by this property access
   */
  RecordingDelegate recordPropertyRead(final String name) {
    Expression expression = Expression.sentenceRoot()
    ExpressionRecording recording = new ExpressionRecording(expression, registry)
    recording.recordPropertyRead(name)
    RecordingDelegate result = new RecordingDelegate(recording)
    registry.add(expression, result)
    result
  }

  /**
   * Records the specified method call as a new
   * top-level sentence (this is the only way
   * new sentences are created).
   * Before adding it to the list of sentences,
   * all unused Expressions in the registry
   * will be added to the list of sentences.
   * @param name the name of the property
   * @param args the arguments passed to the method
   * @return the recording delegate for the
   * {@link Expression} created by this method call
   */
  RecordingDelegate recordMethodCall(final String name, final List args) {
    Expression expression = Expression.sentenceRoot()
    ExpressionRecording recording = new ExpressionRecording(expression, registry)
    // the call must be recorded before the unused
    // Expressions from the registry are converted
    // into sentences (because args might contain
    // some of them)
    recording.recordMethodCall(name, args)
    // now move all remaining Expressions from the
    // registry to sentences
    convertRegistrySentences()
    // and finally add the new sentence
    sentences << expression
    RecordingDelegate result = new RecordingDelegate(recording)
    result
  }

  /**
   * This method takes care of unfinished sentences
   * and the like. It should be called after the
   * Closure has been executed.
   */
  void finishRecording() {
    convertRegistrySentences()
  }

  /**
   * converts all expressions currently held in
   * the registry into sentences.
   */
  private void convertRegistrySentences() {
    sentences.addAll(registry.expressions)
    registry.expressions.clear()
  }

}
