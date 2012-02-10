package de.keelin.slang.closurerecorder

import spock.lang.Specification
import de.keelin.slang.domain.CallType
import de.keelin.slang.domain.CallWithSubexpressions
import de.keelin.slang.domain.ExpressionType
import static de.keelin.slang.domain.ExpressionTestUtil.*
import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.ExpressionTestUtil

import static de.keelin.slang.domain.Expression.*
import de.keelin.slang.domain.ExpressionRole

/**
 * Date: 31.01.12
 * Time: 16:09
 */
class ExpressionRecordingSpec extends Specification {

  DelegatePropertyRegistry registry = new DelegatePropertyRegistry()
  ExpressionRecording recording = new ExpressionRecording(sentenceRoot(), registry)

  def "recordMethodCall() records a simple method call" () {
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
    given: "a Map containing method parameters including one RecordingDelegate..."
    Expression paramExpression = ExpressionTestUtil.propertyRead("value2")
    ExpressionRecording paramRec = new ExpressionRecording(paramExpression, registry)
    RecordingDelegate param = new RecordingDelegate(paramRec)
    Map params = [key1:"value1", key2:param]
    and: "that ExpressionRecording is also registered with the reording's DelegatePropertyRegistry"
    registry.add(paramExpression, param)
    when: "recordMethodCall() is called with that parameter Map"
    recording.recordMethodCall("testMethod", [params])
    then: "the contents of the parameter Map are resolved correctly, including the expressions"
    recording.words ==
        ["testMethod", "key1", "value1", "key2", "value2"] ||
      recording.words ==
        ["testMethod", "key2", "value2", "key1", "value1"]
    and: "the expressions used as parameters have switched their role according to their usage"
    paramExpression.role == ExpressionRole.MAP_VALUE
    and: "the expressions used as parameters have been removed from the registry"
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
    given: "an Expression and a wrapping RecordingDelegate (for the method parameter)"
    Expression paramExpression = propertyRead("param1")
    ExpressionRecording paramRec = new ExpressionRecording(paramExpression, registry)
    RecordingDelegate param = new RecordingDelegate(paramRec)
    registry.add(paramExpression, param)
    when : "recordMethod() is called with that ExpressionRecording as a parameter"
    recording.recordMethodCall("testMethod", [param])
    then : "the param is not registered with the registry anymore"
    registry.expressions == []
    and: "the ExpressionRecording holds the specified Expression as parameter"
    recording.expression.calls[0].subexpressions[0] == paramExpression
    and: "the parameter epression has switched roles"
    paramExpression.role == ExpressionRole.METHOD_PARAM
  }

  def "recordPropertyRead() records a simple propertyRead" () {
    given: "a recorder that has already recorded a method call"
    recording.recordMethodCall("prepMethod", ["prepParam1", "prepParam2"])
    when : "the recording shall record a simple property read"
    recording.recordPropertyRead("testProp")
    then : "both the method call and the property read are recorded"
    recording.size == 4
    recording.expression.calls.size() == 2
    recording.words == ["prepMethod", "prepParam1", "prepParam2", "testProp"]
    recording.expression.calls[1].type == CallType.PROPERTY_READ
  }

  def "recordPropertyRead() and recordMethodCall() both return null" () {
    when: "recordPropertyRead() and recordMethodCall() are called"
    def methodResult = recording.recordMethodCall("testMethod", ["parameter1"])
    def propResult = recording.recordPropertyRead("testProp")
    then: "the result is always null"
    methodResult == null
    propResult == null
  }

}
