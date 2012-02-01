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
    ExpressionRecording rec1 = new ExpressionRecording(exp1)
    Expression exp2 = propertyRead("prop2")
    ExpressionRecording rec2 = new ExpressionRecording(exp2)
    registry.add(exp2, rec2)
    registry.add(exp1, rec1)
    then:
    registry.getExpression(rec1).is(exp1)
    registry.getExpression(rec2).is(exp2)
  }

  def "getExpressions() returns a List of expressions in the exact order of registration" () {
    when: "a couple Expressions are aded to the registry"
    Expression exp1 = propertyRead("prop1")
    ExpressionRecording rec1 = new ExpressionRecording(exp1)
    Expression exp2 = propertyRead("prop2")
    ExpressionRecording rec2 = new ExpressionRecording(exp2)
    Expression exp3 = propertyRead("prop3")
    ExpressionRecording rec3 = new ExpressionRecording(exp3)
    Expression exp4 = propertyRead("prop4")
    ExpressionRecording rec4 = new ExpressionRecording(exp4)
    registry.add(exp2, rec2)
    registry.add(exp1, rec1)
    registry.add(exp3, rec3)
    registry.add(exp4, rec4)
    then:
    registry.getExpressions() == [exp2, exp1, exp3, exp4]
  }

  def "remove() will remove the exact expression specified for both kinds of access" () {
    given:
    Expression exp1 = propertyRead("prop")
    ExpressionRecording rec1 = new ExpressionRecording(exp1)
    Expression exp2 = propertyRead("prop")
    ExpressionRecording rec2 = new ExpressionRecording(exp2)
    Expression exp3 = propertyRead("prop")
    ExpressionRecording rec3 = new ExpressionRecording(exp3)
    Expression exp4 = propertyRead("prop")
    ExpressionRecording rec4 = new ExpressionRecording(exp4)
    registry.add(exp2, rec2)
    registry.add(exp1, rec1)
    registry.add(exp3, rec3)
    registry.add(exp4, rec4)
    when:
    registry.remove(rec3)
    then:
    registry.expressions == [exp2, exp1, exp4]
    registry.expressions != [exp2, exp1, exp3]
    registry.getExpression(rec3) == null
  }
}
