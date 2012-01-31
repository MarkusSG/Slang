package de.keelin.slang.domain

/**
 * Date: 31.01.12
 * Time: 12:41
 */
class Call {

  final def value
  final Expression parent
  final CallType type

  static Call propertyRead(final String name, final Expression parent) {
    new Call(name, CallType.PROPERTY_READ, parent)
  }

  static Call objectRef(final value, final Expression parent) {
    new Call(value, CallType.OBJECT_REF, parent)
  }

  static Call propertyAssignment(final String name, final Expression parent, final Expression value) {
    new CallWithSubexpressions(name, CallType.PROPERTY_ASSIGNMENT, parent, value)
  }

  static Call method(final String name,  final Expression parent, final List<Expression> params) {
    new CallWithSubexpressions(name, CallType.METHOD, parent, params)
  }

  static Call method(final String name,  final Expression parent) {
    method(name, parent, [])
  }

  Call(final value, final CallType type, final Expression parent) {
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

  CallWithSubexpressions(value, CallType type, Expression parent, List<Expression> subexpressions) {
    super(value, type, parent)
    this.subexpressions = subexpressions
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
