package de.keelin.slang.domain
/**
 * Base class of all Calls.
 * A Call can represent a property
 * read (like myObj.property) or an
 * object reference (f.e. in a method
 * call's parameter list), but not
 * a property write or a method call
 * (because these contain
 * subexpressions).
 *
 * @see CallWithSubexpressions
 * @see Expression
 *
 * @author Markus Günther
 */

class Call {

  /**
   * The name of the property or method
   * (in case of a property access or a
   * method call) or the referenced object
   */
  final def value

  /**
   * Describes the Object, upon which
   * this call was executed
   */
  final CallOrigin origin

  /**
   * Describes, what kind of Call this is
   */
  final CallType type

  /**
   * creates a Call representing a property read.
   * @param name the name of the property
   * @param origin the CallOrigin describing the
   * owner of the property
   * @return the specified Call
   */
  static Call propertyRead(final String name, final CallOrigin origin) {
    new Call(name, CallType.PROPERTY_READ, origin)
  }

  /**
   * creates a Call representing an object reference.
   * (probably a method parameter
   * @param value the object
   * @return the specified Call
   */
  static Call objectRef(final value) {
    new Call(value, CallType.OBJECT_REF, CallOrigin.NONE)
  }

  /**
   * Creates a Call representing a property assignment.
   * @param name the name of the property
   * @param origin the CallOrigin describing the property
   * @param value the value assigned to the property
   * @return the specified Call
   */
  static CallWithSubexpressions propertyAssignment(final String name, final CallOrigin origin, final Expression value) {
    new CallWithSubexpressions(name, CallType.PROPERTY_ASSIGNMENT, origin, [value])
  }

  /**
   * Creates a Call representing a method call.
   * @param name the name of the method
   * @param origin the CallOrigin describing the owner of the method
   * @param params the parameters the method was called with
   * @return the specified Call
   */
  static CallWithSubexpressions method(final String name,  final CallOrigin origin, final List<Expression> params) {
    new CallWithSubexpressions(name, CallType.METHOD, origin, params)
  }

  /**
   * Creates a Call representing a method call with no arguments.
   * @param name the name of the method
   * @param origin the CallOrigin describing the owner of the method
   * @return the specified Call
   */
  static CallWithSubexpressions method(final String name, final CallOrigin parent) {
    method(name, parent, [])
  }

  /**
   * creates a Call to represent a MapEntry with the
   * Call's value as the key and the only subexpression
   * as the value.
   * @param key the MapEntry's key
   * @param value the Expression wrapping MapEntry's value
   * @return the specified Call
   */
  static CallWithSubexpressions mapEntry(key, Expression value) {
    new CallWithSubexpressions(key, CallType.MAP_ENTRY, CallOrigin.NONE, [value])
  }

  /**
   * creates a Call to represent a MapEntry with the
   * Call's value as the key and the only subexpression
   * as the value.
   * @param key the MapEntry's key
   * @param value the MapEntry's value
   * @return the specified Call
   */
  static CallWithSubexpressions mapEntry(key, value) {
    Expression valueExpression = Expression.methodParamCallChain()
    valueExpression.calls << objectRef(value)
    CallWithSubexpressions result = new CallWithSubexpressions(key, CallType.MAP_ENTRY, CallOrigin.NONE, [valueExpression])
    valueExpression.parent = result
    result
  }

  /**
   * Creates a new Call with the specified properties.
   * Usually it's preferable to use one of the static
   * factory methods.
   * @param value the value of the call (property name,
   * method name, etc.)
   * @param type describes, what kind of Call
   * (propertyAssignment, method call, ...) shall be
   * created
   * @param origin describes hich object the call has been
   * made upon
   * @see #propertyRead(String, CallOrigin)
   * @see #objectRef(Object)
   * @see #propertyAssignment(String, CallOrigin, Expression)
   * @see #method(String, CallOrigin)
   * @see #method(String, CallOrigin, List<Expression>)
   * @see #mapEntry(Object, Expression)
   * @see #mapEntry(Object, Object)
   */
  Call(final value, final CallType type, final CallOrigin origin) {
    this.value = value
    this.origin = origin
    this.type = type
  }

  /**
   * returns a textual representation of this call and
   * all its subexpressions. Only words will be returned
   * (i.e. interpunction is omitted)
   * @return a List containing one String for each word
   */
  List<String> getWords() {
    [value.toString()]
  }

}

/**
 * This class represents all calls that have some kind
 * of "parameters", f.e. method calls, property
 * assignments or Map values.
 *
 */
class CallWithSubexpressions extends Call {

  /**
   * holds the {@link Expression} representing the
   * parameters, assignment value or MapEntry value
   */
  final List<Expression> subexpressions = []

  /**
   * Creates a new CallWithSubexpressions with the
   * specified properties. Usually it's preferable
   * to use one of the static factory methods.
   * @param value the value of the call (property name,
   * method name, etc.)
   * @param type describes, what kind of Call
   * (propertyAssignment, method call, ...) shall be
   * created
   * @param origin describes hich object the call has been
   * made upon
   * @param subexpressions the parameters, assignment
   * value or MapEntry value
   */
  CallWithSubexpressions(value, CallType type, CallOrigin origin, List<Expression> subexpressions) {
    super(value, type, origin)
    this.subexpressions = subexpressions
    subexpressions.each {Expression subExpression ->
      subExpression.parent = this
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  List<String> getWords() {
    List result = super.getWords()
    result += subexpressions.collect {Expression expression ->
      expression.words
    }.flatten()
    result
  }

}
