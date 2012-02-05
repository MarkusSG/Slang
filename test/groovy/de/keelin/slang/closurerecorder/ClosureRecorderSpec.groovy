package de.keelin.slang.closurerecorder

import spock.lang.Specification
import de.keelin.slang.domain.Expression

/**
 * Date: 05.02.12
 * Time: 13:03
 */
class ClosureRecorderSpec extends Specification {

  ClosureRecorder recorder = new ClosureRecorder()

  def "Recorder records a closure properly" () {
    given: "a closure with a few external sentences"
    def closure = {
      the quick, brown fox
      jumps over: the lazy dog
    }
    when: "record() is called on the recorder"
    List<Expression> result = recorder.record(closure)
    then: "the rescording matches the input closure"
    result.size() == 2
    result[0].words == ["the", "quick", "brown", "fox"]
    result[1].words == ["jumps", "over", "the", "lazy", "dog"]
  }

  def "the closure's original delegate is restored after recording" () {
    given: "a closure with a delegate"
    def delegate = new Object()
    def closure = {->}
    closure.setDelegate(delegate)
    when: "record() is called on the recorder"
    recorder.record(closure)
    then: "the closure's delegate is the same as before"
    delegate == closure.delegate
  }

}
