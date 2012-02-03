package de.keelin.slang.closurerecorder

import spock.lang.Specification

import de.keelin.slang.domain.ExpressionType
import de.keelin.slang.domain.ExpressionRole

/**
 * Date: 02.02.12
 * Time: 16:51
 */
class ClosureRecordingSpec extends Specification {

  DelegatePropertyRegistry registry = new DelegatePropertyRegistry()
  ClosureRecording recording = new ClosureRecording(registry)

    def "recordPropertyRead() returns a new ExpressionRecordingDelegate for the recorded property Read" () {
      when : "recordPropertyRead() is called"
      ExpressionRecordingDelegate expressionDelegate = recording.recordPropertyRead("someProperty")
      then : "the registry holds an Expression for the returned delegate"
      registry.getExpression(expressionDelegate)
      and: "that Expression matches the recorded method call"
      registry.getExpression(expressionDelegate).role == ExpressionRole.SENTENCE_ROOT
      registry.getExpression(expressionDelegate).type == ExpressionType.CALL_CHAIN
      registry.getExpression(expressionDelegate).words == ["someProperty"]
    }

    def "recordMethodCall() returns a new ExpressionRecordingDelegate for the recorded method call" () {
      when : "recordMethodCall() is called"
      ExpressionRecordingDelegate expressionDelegate = recording.recordMethodCall("someMethod", ["param1"])
      then : "the recording holds a reference to the created Expression / sentence"
      recording.sentences[0].words == ["someMethod", "param1"]
      recording.sentences[0].role == ExpressionRole.SENTENCE_ROOT
      recording.sentences[0].type == ExpressionType.CALL_CHAIN
      and: "the registry doesn't"
      registry.getExpression(expressionDelegate) == null
    }

  def "recordMethodCall converts all unused root property accesses to new sentences (in the correct order)" () {
    given: "a recording with a couple of unused property reads in the registry"
    recording.recordPropertyRead("prop1")
    recording.recordPropertyRead("prop2")
    ExpressionRecordingDelegate parameter = recording.recordPropertyRead("prop3")
    when: "recordMethodCall() is called, using one of the property reads as parameter"
    recording.recordMethodCall("method", [parameter])
    then: "the registry is empty"
    registry.expressions.isEmpty()
    and: "all property reads have been converted into sentences before the method call was added"
    recording.sentences.size() == 3
    recording.sentences[0].words == ["prop1"]
    recording.sentences[1].words == ["prop2"]
    recording.sentences[2].words == ["method", "prop3"]
  }
}
