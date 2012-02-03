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
}
