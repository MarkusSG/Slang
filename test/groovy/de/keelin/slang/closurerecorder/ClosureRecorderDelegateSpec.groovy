package de.keelin.slang.closurerecorder

import spock.lang.Specification

/**
 * Date: 10.01.12
 * Time: 17:00
 */
class ClosureRecorderDelegateSpec extends Specification {

    List<Call> recordedCalls = []
    ClosureRecorderDelegate delegate = new ClosureRecorderDelegate(recordedCalls)

    def "records all external calls from a closure if used as it's delegate " () {
      given: "a closure with a few external calls"
        def closure = {the quick, brown fox}
      when: "the delegate is set as the closure's delegate"
        closure.setDelegate(delegate)
      and: "the closure is executed"
        closure()
      closure.setDelegate(null)
      then: "the recorded calls match the external calls of the closure"
        recordedCalls[0].type == Call.Type.CALL_METHOD
        recordedCalls[0].name == "the"
        recordedCalls[0].args == ["quick", "brown"]
        recordedCalls[1].type == Call.Type.READ_PROPERTY
        recordedCalls[1].name == "fox"
    }
}
