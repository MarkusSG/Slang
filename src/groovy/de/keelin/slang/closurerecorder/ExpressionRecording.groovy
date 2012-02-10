package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.Call
import de.keelin.slang.domain.CallWithSubexpressions
import de.keelin.slang.domain.ExpressionRole
import de.keelin.slang.domain.CallOrigin

import static de.keelin.slang.domain.Expression.*
import static de.keelin.slang.domain.Call.*

/**
 * Recording class for a single {@link Expression}.
 * the record...() methods of this class returns null
 * because the wrapping {@link RecordingDelegate)
 * is needed to intercept subsequent calls.
 * original caller of the recorded method / property, so
 * further calls on these can be told from top-level ones.
 *
 * @see ClosureRecording
 * @see RecordingDelegate
 *
 * @author Markus Günther
 */
class ExpressionRecording implements Recording{

  Expression expression

  /**
   * This is used to resolve expressions used as method
   * parameters or assignment values.
   */
  DelegatePropertyRegistry delegatePropertyRegistry

  /**
   * The origin of the next recorded interaction
   * (first one in an Expression is always DELEGATE,
   * others always PREDECESSOR).
   */
  CallOrigin origin = CallOrigin.DELEGATE


  ExpressionRecording(final Expression expression, final DelegatePropertyRegistry registry) {
    this.expression = expression
    delegatePropertyRegistry = registry
  }

  /**
   * returns the number of words represented by the Expression
   */
  int getSize() {
    getWords().size()
  }

  /**
   * returns a List of the words represented by the Expression
   */
  List<String> getWords() {
    expression.words
  }

  /**
   * Records the specified method call.
   * If args contains ay RecordingDelegates,
   * they will be replaced by the corresponding
   * Expressions (which in turn will be removed
   * from the registry).
   * @param name the name of the method
   * @param args the arguments passed to the method
   * @return null (RecordingDelegate must return itself)
   */
  def recordMethodCall(String name, List params) {
    initRootExpression()
    CallWithSubexpressions method = method(name, origin)
    origin = CallOrigin.PREDECESSOR
    expression.calls << method
    method.subexpressions.addAll(convertParams(params, method))
  }

  /**
   * Records the specified propertyRead.
   * It is not registered in the registry,
   * but immediately appended to the call
   * chain.
   * @param name the name of the property
   * @return null (RecordingDelegate must return itself)
   */
  def recordPropertyRead(String name) {
    initRootExpression()
    expression.calls << propertyRead(name, origin)
    origin = CallOrigin.PREDECESSOR
  }

  private void initRootExpression() {
    if (!expression) {
      expression = methodParamCallChain()
    }
  }

  /**
   * Wraps method parameters in the correct kinds
   * of Expressions
   * @param params the method parameters
   * @param parent the Call describing the method call
   * @return a List of Expressions representing the
   * original parameters
   */
  private List<Expression> convertParams(params, Call parent) {
    params.collect {
      if (it instanceof RecordingDelegate) {
        // top level property: get Expression from registry
        Expression expression = delegatePropertyRegistry?.remove(it)
        expression.role = ExpressionRole.METHOD_PARAM
        expression.parent = parent
        expression
      } else if (it instanceof Map) {
        // create Map expression and handle all
        // map entries individually
        Expression mapExpression = methodParamMap()
        mapExpression.parent = parent
        it.each {key, value ->
          Expression valueExpression
          if (value instanceof RecordingDelegate) {
            // top level property: get Expression from registry
            valueExpression = delegatePropertyRegistry.remove(value)
          } else {
            // simple pogo: wrap in call chain Expression
            valueExpression = Expression.methodParamCallChain()
            valueExpression.calls << objectRef(value)
          }
          mapExpression.calls << Call.mapEntry(key, valueExpression)
          valueExpression.role = ExpressionRole.MAP_VALUE
          valueExpression.parent = mapExpression.calls[-1]
        }
        mapExpression
      } else {
        // simple pogo: wrap in call chain Expression
        Expression ex = methodParamCallChain()
        ex.parent = parent
        ex.calls << objectRef(it)
        ex
      }
    }
  }

}
