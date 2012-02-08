package de.keelin.slang.closurerecorder

import spock.lang.Specification
import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.ExpressionTestUtil
import de.keelin.slang.domain.CallOrigin
import de.keelin.slang.domain.CallType

/**
 * Date: 05.02.12
 * Time: 13:03
 */
class ClosureRecorderSpec extends Specification {

  ClosureRecorder recorder = new ClosureRecorder()

  def "Recorder records a closure properly" () {
    given: "a closure with a few external sentences"
    def closure = {
      feral
      the quick, brown fox
      jumps over: the lazy dog
      yeah
    }
    when: "record() is called on the recorder"
    List<Expression> result = recorder.record(closure)
    then: "the rescording matches the input closure"
    result.size() == 4
    result[0].words == ["feral"]
    result[1].words == ["the", "quick", "brown", "fox"]
    result[2].words == ["jumps", "over", "the", "lazy", "dog"]
    result[3].words == ["yeah"]
    and: "is structurally sound"
    ExpressionTestUtil.checkHierarchy(result[0]) == []
    ExpressionTestUtil.checkHierarchy(result[1]) == []
    ExpressionTestUtil.checkHierarchy(result[2]) == []
    ExpressionTestUtil.checkHierarchy(result[3]) == []
    // feral
    result[0].calls[0].parent == CallOrigin.DELEGATE
    // the
    result[1].calls[0].parent == CallOrigin.DELEGATE
    // fox
    result[1].calls[1].parent == CallOrigin.PREDECESSOR
    result[1].calls[1].type == CallType.PROPERTY_READ
    // over
    result[2].calls[0].subexpressions[0].calls[0].parent == CallOrigin.NONE
    result[2].calls[0].subexpressions[0].calls[0].type == CallType.MAP_ENTRY
    // the
    result[2].calls[0].subexpressions[0].calls[0].subexpressions[0].calls[0].parent == CallOrigin.DELEGATE
    result[2].calls[0].subexpressions[0].calls[0].subexpressions[0].calls[0].type == CallType.PROPERTY_READ
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
