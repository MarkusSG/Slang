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
    expression.calls << propertyRead(name, CallOrigin.DELEGATE)
    expression
  }

  static def checkHierarchy(Expression expression) {
    List errors = []
    // check that SENTENCE_ROOT has no parent and everyone else does
    if ((expression.parent == null ^ expression.role == ExpressionRole.SENTENCE_ROOT)) {
      errors << "1: Expression with words [${expression.words}] has role ${expression.role} and parent ${expression.parent}."
    }
    // check that method params have method calls as parents
    if (expression.role == ExpressionRole.METHOD_PARAM && expression.parent?.type != CallType.METHOD ) {
      errors << "2: Expression with words [${expression.words}] has role ${expression.role} and a parent of type ${expression.parent?.type}."
    }
    // check that only method params have method calls as parents
    if (expression.role != ExpressionRole.METHOD_PARAM && expression.parent?.type == CallType.METHOD ) {
      errors << "3: Expression with words [${expression.words}] has role ${expression.role} and a parent of type METHOD_CALL."
    }
    // check that Map values have Map entries as parents
    if (expression.role == ExpressionRole.MAP_VALUE && expression.parent?.type != CallType.MAP_ENTRY) {
      errors << "4: Expression with words [${expression.words}] has role ${expression.role} and a parent of type ${expression.parent?.type}."
    }
    // check that only Map values have Map entries as parents
    if (expression.role != ExpressionRole.MAP_VALUE && expression.parent?.type == CallType.MAP_ENTRY) {
      errors << "5: Expression with words [${expression.words}] has role ${expression.role} and a parent of type MAP_ENTRY."
    }
    // check calls
    expression.calls.each {call ->
      if (call instanceof Call) {
        if (expression.type == ExpressionType.MAP && call.type != CallType.MAP_ENTRY) {
          errors << "6: Expression with words [${expression.words}] has type MAP and a child of type ${call.type}."
        }
        if (!call.value) {
          errors << "7: Call (at the beginning of ${call.words}) has no value"
        }
        if (!call.origin) {
          errors << "8: ${call.value} (at the beginning of ${call.words}) has no origin"
        }
        if (!call.type) {
          errors << "9: ${call.value} (at the beginning of ${call.words}) has no type"
        }
        if (call instanceof CallWithSubexpressions){
          errors.addAll(checkHierarchy(call))
        }
      } else if (call instanceof Expression){
        errors << "10: Expression ${call.type}:${expression.words} is a child of Expression {${expression.type}:${expression.words}}"
      } else {
        errors << "11: Object ${call.class.name} is a child of Expression {${expression.type}}"
      }
    }
    errors
  }

  private static def checkHierarchy(CallWithSubexpressions call) {
    List errors = []
    call.subexpressions.each {expression ->
      if (expression instanceof Expression) {
        if (expression.parent != call) {
          errors << "12: ${expression.type}:${expression.words} is a child of {${call.value}}, yet it's parent is ${expression.parent}"
        }
        if (!expression.type) {
          errors << "13: Expression at the beginning of ${expression.words} has no type"
        }
        if (!expression.calls) {
          errors << "14: Expression (child of ${expression.parent.value}) has no calls"
        }
        if (!expression.role) {
          errors << "15: Expression at the beginning of ${expression.words} has no role"
        }
        errors.addAll(checkHierarchy(expression))
      } else if (expression instanceof Call){
        errors << "${expression.value} is a child of {${call.value}}"
      } else {
        errors << "16: Object ${expression.class.name} is a child of Expression {${call.value}}"
      }
    }
    errors
  }
}
