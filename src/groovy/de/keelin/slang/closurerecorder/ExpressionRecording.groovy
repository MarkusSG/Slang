package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.Call
import de.keelin.slang.domain.CallWithSubexpressions

import static de.keelin.slang.domain.Expression.*
import static de.keelin.slang.domain.Call.*
import de.keelin.slang.domain.ExpressionRole
/**
 * Date: 31.01.12
 * Time: 16:10
 */
class ExpressionRecording implements Recording{

  Expression expression
  DelegatePropertyRegistry delegatePropertyRegistry

  ExpressionRecording(Expression expression, DelegatePropertyRegistry registry) {
    this.expression = expression
    delegatePropertyRegistry = registry
  }

  int getSize() {
    getWords().size()
  }

  List<String> getWords() {
    expression.words
  }

  def recordMethodCall(String name, List params) {
    initRootExpression()
    CallWithSubexpressions method = method(name,expression)
    expression.calls << method
    method.subexpressions.addAll(convertParams(params, method))
  }

  def recordPropertyRead(String name) {
    initRootExpression()
    expression.calls << propertyRead(name, expression)
  }

  private void initRootExpression() {
    if (!expression) {
      expression = methodParamCallChain(null)
    }
  }

  private List<Expression> convertParams(params, Call parent) {
    params.collect {
      if (it instanceof RecordingDelegate) {
        Expression expression = delegatePropertyRegistry?.remove(it)
        expression.role = ExpressionRole.METHOD_PARAM
        expression.parent = parent
        expression
      } else if (it instanceof Map) {
        Expression mapExpression = methodParamMap(parent)

        it.each {key, value ->
          Expression valueExpression
          if (value instanceof RecordingDelegate) {
            valueExpression = delegatePropertyRegistry.remove(value)
          } else {
            valueExpression = Expression.methodParamCallChain(null)
            valueExpression.calls << objectRef(value, null)
          }
          mapExpression.calls << Call.mapEntry(key, valueExpression, mapExpression)
          valueExpression.role = ExpressionRole.MAP_VALUE
          valueExpression.parent = mapExpression.calls[-1]
        }
        mapExpression
      } else {
        Expression ex = methodParamCallChain(parent)
        ex.calls << objectRef(it, ex)
        ex
      }
    }
  }

}
