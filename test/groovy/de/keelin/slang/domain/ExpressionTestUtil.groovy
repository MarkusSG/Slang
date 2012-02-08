package de.keelin.slang.domain

import static Expression.*
import static de.keelin.slang.domain.Call.*

/**
 * Date: 01.02.12
 * Time: 11:32
 */
class ExpressionTestUtil {

  static Expression propertyRead(String name) {
    Expression expression = sentenceRoot()
    expression.calls << propertyRead(name, expression)
    expression
  }

  static def checkHierarchy(Expression expression) {
    List errors = []
    expression.calls.each {call ->
      if (call instanceof Call) {
        if (call instanceof CallWithSubexpressions){
          errors.addAll(checkHierarchy(call))
        }
      } else if (call instanceof Expression){
        errors << "Expression ${call.type}:${expression.words} is a child of Expression {${expression.type}:${expression.words}}"
      } else {
        errors << "Object ${call.class.name} is a child of Expression {${expression.type}}"
      }
    }
    errors
  }

  private static def checkHierarchy(CallWithSubexpressions call) {
    List errors = []
    call.subexpressions.each {expression ->
      if (expression instanceof Expression) {
        if (expression.parent != call) {
          errors << "${expression.type}:${expression.words} is a child of {${call.value}}, yet it's parent is ${expression.parent}"
        } else {
            errors.addAll(checkHierarchy(expression))
        }
      } else if (expression instanceof Call){
        errors << "${expression.value} is a child of {${call.value}}"
      } else {
        errors << "Object ${expression.class.name} is a child of Expression {${call.value}}"
      }
    }
    errors
  }
}
