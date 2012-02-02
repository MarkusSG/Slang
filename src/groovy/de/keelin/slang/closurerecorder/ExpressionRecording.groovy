package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.Call
import de.keelin.slang.domain.CallWithSubexpressions

import static de.keelin.slang.domain.Expression.*
import static de.keelin.slang.domain.Call.*
/**
 * Date: 31.01.12
 * Time: 16:10
 */
class ExpressionRecording {

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
      if (it instanceof ExpressionRecordingDelegate) {
        delegatePropertyRegistry?.remove(it)
      } else if (it instanceof Map) {
        Expression ex = methodParamMap(parent)
        it.each {key, value ->
          ex.calls << objectRef(key, ex)
          if (value instanceof ExpressionRecordingDelegate) {
            ex.calls << delegatePropertyRegistry.remove(value)
          } else {
            ex.calls << objectRef(value, ex)
          }
        }
        ex
      } else {
        Expression ex = methodParamCallChain(parent)
        ex.calls << objectRef(it, ex)
        ex
      }
    }
  }

}
