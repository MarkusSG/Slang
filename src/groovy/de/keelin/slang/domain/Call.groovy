package de.keelin.slang.domain

import static CallOrigin.*
/**
 * Date: 31.01.12
 * Time: 12:41
 */
class Call {

  final def value
  final CallOrigin parent
  final CallType type

  static Call propertyRead(final String name, final CallOrigin parent) {
    new Call(name, CallType.PROPERTY_READ, parent)
  }

  static Call objectRef(final value, final CallOrigin parent) {
    new Call(value, CallType.OBJECT_REF, parent)
  }

  static CallWithSubexpressions propertyAssignment(final String name, final CallOrigin parent, final Expression value) {
    new CallWithSubexpressions(name, CallType.PROPERTY_ASSIGNMENT, parent, [value])
  }

  static CallWithSubexpressions method(final String name,  final CallOrigin parent, final List<Expression> params) {
    new CallWithSubexpressions(name, CallType.METHOD, parent, params)
  }

  static CallWithSubexpressions method(final String name, final CallOrigin parent) {
    method(name, parent, [])
  }

  static CallWithSubexpressions mapEntry(key, Expression value) {
    new CallWithSubexpressions(key, CallType.MAP_ENTRY, NONE, [value])
  }

  static CallWithSubexpressions mapEntry(key, value) {
    Expression valueExpression = Expression.methodParamCallChain(null)
    valueExpression.calls << objectRef(value, null)
    CallWithSubexpressions result = new CallWithSubexpressions(key, CallType.MAP_ENTRY, NONE, [valueExpression])
    valueExpression.parent = result
    result
  }

  Call(final value, final CallType type, final CallOrigin parent) {
    this.value = value
    this.parent = parent
    this.type = type
  }

  List<String> getWords() {
    [value.toString()]
  }

}

class CallWithSubexpressions extends Call {

  final List<Expression> subexpressions = []

  CallWithSubexpressions(value, CallType type, CallOrigin parent, List<Expression> subexpressions) {
    super(value, type, parent)
    this.subexpressions = subexpressions
    subexpressions.each {Expression subExpression ->
      subExpression.parent = this
    }
  }

  @Override
  List<String> getWords() {
    List result = super.getWords()
    result += subexpressions.collect {Expression expression ->
      expression.words
    }.flatten()
    result
  }

}
