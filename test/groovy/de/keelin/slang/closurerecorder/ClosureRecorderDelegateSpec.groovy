package de.keelin.slang.closurerecorder

import spock.lang.Specification

/**
 * Date: 10.01.12
 * Time: 17:00
 */
class ClosureRecorderDelegateSpec extends Specification {

  List<List<Call>> recordedCalls = []
  ClosureRecorderDelegate delegate = new ClosureRecorderDelegate(recordedCalls)

  def "records all external calls from a closure if used as it's delegate "() {
    given: "a closure with a few external sentences"
    def closure = {
      the quick, brown fox
      jumps over: the lazy dog
    }
    when: "the delegate is set as the closure's delegate"
    closure.setDelegate(delegate)
    and: "the closure is executed"
    closure()
    closure.setDelegate(null)
    then: "the recorded sentences match the external sentences of the closure"
    recordedCalls.size() == 2
    recordedCalls[0].size() == 2
    recordedCalls[0][0].type == Call.Type.CALL_METHOD
    recordedCalls[0][0].name == "the"
    recordedCalls[0][0].args == ["quick", "brown"]
    recordedCalls[0][1].type == Call.Type.READ_PROPERTY
    recordedCalls[0][1].name == "fox"
    recordedCalls[1].size() == 2
    recordedCalls[1][0].type == Call.Type.CALL_METHOD
    recordedCalls[1][0].name == "jumps"
    recordedCalls[1][0].args == [[over:"the"]]
    recordedCalls[1][1].type == Call.Type.CALL_METHOD
    recordedCalls[1][1].name == "lazy"
    recordedCalls[1][1].args == ["dog"]
  }
}
