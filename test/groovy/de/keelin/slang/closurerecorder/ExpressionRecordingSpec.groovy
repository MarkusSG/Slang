package de.keelin.slang.closurerecorder

import spock.lang.Specification
import de.keelin.slang.domain.CallType
import de.keelin.slang.domain.CallWithSubexpressions
import de.keelin.slang.domain.ExpressionType
import static de.keelin.slang.domain.ExpressionTestUtil.*
import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.ExpressionTestUtil

import static de.keelin.slang.domain.Expression.*
/**
 * Date: 31.01.12
 * Time: 16:09
 */
class ExpressionRecordingSpec extends Specification {

  DelegatePropertyRegistry registry = new DelegatePropertyRegistry()
  ExpressionRecording recording = new ExpressionRecording(sentenceRoot(), registry)

  def "recordMethod() records a simple method call" () {
    when : "the recording shall record a method call with one String parameter"
    recording.recordMethodCall("testMethod", ["parameter1"])
    then :
    recording.size == 2
    recording.words == ["testMethod", "parameter1"]
    recording.expression.calls.size() == 1
    recording.expression.calls[0] instanceof CallWithSubexpressions
    recording.expression.calls[0].type == CallType.METHOD
    recording.expression.calls[0].subexpressions.size() == 1
    recording.expression.calls[0].subexpressions[0].type == ExpressionType.CALL_CHAIN
    recording.expression.calls[0].subexpressions[0].calls.size == 1
    recording.expression.calls[0].subexpressions[0].calls[0].type == CallType.OBJECT_REF
  }

  def "recordMethod() can handle Map-parameters containing ExpressionRecordingDelegates as values" () {
    given: "a Map containing method parameters including one ExpressionRecordingDelegate..."
    Expression paramExpression = ExpressionTestUtil.propertyRead("value2")
    ExpressionRecording paramRec = new ExpressionRecording(paramExpression, registry)
    ExpressionRecordingDelegate param = new ExpressionRecordingDelegate(paramRec)
    Map params = [key1:"value1", key2:param]
    and: "that ExpressionRecording is also registered with the reording's DelegatePropertyRegistry"
    registry.add(paramExpression, param)
    when:
    recording.recordMethodCall("testMethod", [params])
    then:
    recording.words ==
        ["testMethod", "key1", "value1", "key2", "value2"] ||
      recording.words ==
        ["testMethod", "key2", "value2", "key1", "value1"]
    registry.expressions.isEmpty()
  }

  def "recordMethod() records a simple method call on top of an existing call before that" () {
    given: "a recorder that has already recorded a propertyRead"
    recording.recordPropertyRead("prepProperty")
    when : "the recording shall record a method call with one String parameter"
    recording.recordMethodCall("testMethod", ["parameter1"])
    then :
    recording.size == 3
    recording.expression.calls.size() == 2
    recording.words == ["prepProperty", "testMethod", "parameter1"]
  }

  def "recordMethod() handles the erasure of parameters from the DelegatePropertyRegistry" () {
    given: "an Expression and a wrapping ExpressionRecordingDelegate (for the method parameter)"
    Expression paramExpression = propertyRead("param1")
    ExpressionRecording paramRec = new ExpressionRecording(paramExpression, registry)
    ExpressionRecordingDelegate param = new ExpressionRecordingDelegate(paramRec)
    registry.add(paramExpression, param)
    when : "recordMethod() is called with that ExpressionRecording as a parameter"
    recording.recordMethodCall("testMethod", [param])
    then : "the param is not registered with the registry anymore"
    registry.expressions == []
    and: "the ExpressionRecording holds the specified Expression as parameter"
    recording.expression.calls[0].subexpressions[0] == paramExpression
  }

  def "recordPropertyRead() records a simple propertyRead" () {
    given: "a recorder that has already recorded a method call"
    recording.recordMethodCall("prepMethod", ["prepParam1", "prepParam2"])
    when : "the recording shall record a simple property read"
    recording.recordPropertyRead("testProp")
    then :
    recording.size == 4
    recording.expression.calls.size() == 2
    recording.words == ["prepMethod", "prepParam1", "prepParam2", "testProp"]
    recording.expression.calls[1].type == CallType.PROPERTY_READ
  }

}