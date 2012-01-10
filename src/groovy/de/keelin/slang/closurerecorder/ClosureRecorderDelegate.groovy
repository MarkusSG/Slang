package de.keelin.slang.closurerecorder

/**
 * Date: 10.01.12
 * Time: 17:01
 */
class ClosureRecorderDelegate implements GroovyInterceptable {

  final List<List<Call>> sentences
  final List propertyReads

  ClosureRecorderDelegate(List<List<Call>> sentences) {
    this.sentences = sentences
    propertyReads = []
  }

  @Override
  def invokeMethod(String name, args) {
    sentences << []
    ClosureSentenceRecorder nextSentence = new ClosureSentenceRecorder(sentences[-1], propertyReads)."$name"(* args)
    sentences.addAll(propertyReads.collect {[it]})
    propertyReads.clear()
    nextSentence
  }

  @Override
  def getProperty(String name) {
    Call call = new Call(Call.Type.READ_PROPERTY, name)
    propertyReads << call
    call
  }
}

private class ClosureSentenceRecorder {

  final List<Call> calls
  final List<Call> propertyReads

  ClosureSentenceRecorder(List<Call> calls, List<Call> propertyReads) {
    this.calls = calls
    this.propertyReads = propertyReads
  }


  @Override
  def invokeMethod(String name, args) {
    // This slightly complicated code is necessary,
    // because accessing propertyReads directly from the collect-closure
    // would result in a getProperty()-call
    def propertyReadsToBeRemoved = []
    def filteredArgs = args.collect { arg ->
      if (arg instanceof Call) {
        propertyReadsToBeRemoved << arg
        arg.name
      } else {
        arg
      }
    }
    propertyReads.removeAll(propertyReadsToBeRemoved)
    // TODO: check if there are property reads left
    calls << new Call(Call.Type.CALL_METHOD, name, filteredArgs)
    this
  }

  @Override
  def getProperty(String name) {
    calls << new Call(Call.Type.READ_PROPERTY, name)
    this
  }
}
