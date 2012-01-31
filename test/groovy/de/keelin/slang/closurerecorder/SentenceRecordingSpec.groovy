package de.keelin.slang.closurerecorder

import spock.lang.Specification
import de.keelin.slang.domain.CallType
import de.keelin.slang.domain.CallWithSubexpressions
import de.keelin.slang.domain.ExpressionType

/**
 * Date: 31.01.12
 * Time: 16:09
 */
class SentenceRecordingSpec extends Specification {

  SentenceRecording recording = new SentenceRecording()

  def "recordMethod() records a simple method call" () {
    when : "the recording shall record a method call with one String parameter"
    recording.recordMethodCall("testMethod", ["parameter1"])
    then :
    recording.size == 2
    recording.words == ["testMethod", "parameter1"]
    recording.rootExpression.calls.size() == 1
    recording.rootExpression.calls[0] instanceof CallWithSubexpressions
    recording.rootExpression.calls[0].type == CallType.METHOD
    recording.rootExpression.calls[0].subexpressions.size() == 1
    recording.rootExpression.calls[0].subexpressions[0].type == ExpressionType.CALL_CHAIN
    recording.rootExpression.calls[0].subexpressions[0].calls.size == 1
    recording.rootExpression.calls[0].subexpressions[0].calls[0].type == CallType.OBJECT_REF
  }

  def "recordMethod() records a simple method call on top of an existing call before that" () {
    given: "a recorder that has already recorded a method call"
    recording.recordMethodCall("prepMethod", ["prepParam1", "prepParam2"])
    when : "the recording shall record a method call with one String parameter"
    recording.recordMethodCall("testMethod", ["parameter1"])
    then :
    recording.size == 5
    recording.rootExpression.calls.size() == 2
    recording.words == ["prepMethod", "prepParam1","prepParam2", "testMethod", "parameter1"]
  }
}
