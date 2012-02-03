package de.keelin.slang.closurerecorder

import spock.lang.Specification
import static de.keelin.slang.domain.ExpressionTestUtil.*
import de.keelin.slang.domain.Expression
/**
 * Date: 01.02.12
 * Time: 11:22
 */
class DelegatePropertyRegistrySpec extends Specification {

  DelegatePropertyRegistry registry = new DelegatePropertyRegistry()

  def "getExpression() returns correct Expression for a given recorder" () {
    when: "two Expressions are aded to the registry"
    Expression exp1 = propertyRead("prop1")
    RecordingDelegate delegate1 = new RecordingDelegate(new ExpressionRecording(exp1, registry))
    Expression exp2 = propertyRead("prop2")
    RecordingDelegate delegate2 = new RecordingDelegate(new ExpressionRecording(exp2, registry))
    registry.add(exp2, delegate2)
    registry.add(exp1, delegate1)
    then:
    registry.getExpression(delegate1).is(exp1)
    registry.getExpression(delegate2).is(exp2)
  }

  def "getExpressions() returns a List of expressions in the exact order of registration" () {
    when: "a couple Expressions are aded to the registry"
    Expression exp1 = propertyRead("prop1")
    RecordingDelegate delegate1 = new RecordingDelegate(new ExpressionRecording(exp1, registry))
    Expression exp2 = propertyRead("prop2")
    RecordingDelegate delegate2 = new RecordingDelegate(new ExpressionRecording(exp2, registry))
    Expression exp3 = propertyRead("prop3")
    RecordingDelegate delegate3 = new RecordingDelegate(new ExpressionRecording(exp3, registry))
    Expression exp4 = propertyRead("prop4")
    RecordingDelegate delegate4 = new RecordingDelegate(new ExpressionRecording(exp4, registry))
    registry.add(exp2, delegate2)
    registry.add(exp1, delegate1)
    registry.add(exp3, delegate3)
    registry.add(exp4, delegate4)
    then: "the expressions-list reflects the exact order in which the Expressions were registered"
    registry.getExpressions() == [exp2, exp1, exp3, exp4]
  }

  def "remove() will remove the exact expression specified for both kinds of access" () {
    given: "a couple entries in the registry"
    Expression exp1 = propertyRead("prop1")
    RecordingDelegate delegate1 = new RecordingDelegate(new ExpressionRecording(exp1, registry))
    Expression exp2 = propertyRead("prop2")
    RecordingDelegate delegate2 = new RecordingDelegate(new ExpressionRecording(exp2, registry))
    Expression exp3 = propertyRead("prop3")
    RecordingDelegate delegate3 = new RecordingDelegate(new ExpressionRecording(exp3, registry))
    Expression exp4 = propertyRead("prop4")
    RecordingDelegate delegate4 = new RecordingDelegate(new ExpressionRecording(exp4, registry))
    registry.add(exp2, delegate2)
    registry.add(exp1, delegate1)
    registry.add(exp3, delegate3)
    registry.add(exp4, delegate4)
    when: "one entry is removed"
    def removeResult = registry.remove(delegate3)
    then: "exactly that and only that entry has been removed"
    registry.expressions == [exp2, exp1, exp4]
    registry.expressions != [exp2, exp1, exp3]
    registry.getExpression(delegate3) == null
    and: "remove() itself has returned the removed Expression"
    removeResult == exp3
  }
}
