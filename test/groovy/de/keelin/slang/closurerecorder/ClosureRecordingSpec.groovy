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

  def "recordPropertyRead() returns a new ExpressionRecordingDelegate for the recorded propertyRead" () {
    when : "recordPropertyRead() is called"
    ExpressionRecordingDelegate expressionDelegate = recording.recordPropertyRead("someProperty")
    then : "the registry holds an Expression for the returned delegate"
    registry.getExpression(expressionDelegate)
    and: "that Expression matches the recorded method call"
    registry.getExpression(expressionDelegate).role == ExpressionRole.SENTENCE_ROOT
    registry.getExpression(expressionDelegate).type == ExpressionType.CALL_CHAIN
    registry.getExpression(expressionDelegate).words == ["someProperty"]
  }
}
