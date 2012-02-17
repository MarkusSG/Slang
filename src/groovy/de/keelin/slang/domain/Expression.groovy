package de.keelin.slang.domain

/**
 * Represents a chain of Calls (or as a special case
 * a Map). Every Call must be wrapped in at least one
 * Expression (see {@link Expression#sentenceRoot()}.
 *
 * Unlike Call, Expression is a merely technical
 * abstraction mostly concerned with syntactical
 * relationships between Calls.
 *
 * @see Call
 *
 * @author Markus Günther
 */
class Expression {

  /**
   * The {@link CallWithSubexpressions}, which
   * uses this Expression as some kind of parameter.
   * If this is null, this Expression is the root
   * of a sentence.
   */
  CallWithSubexpressions parent

  /**
   * Holds the Calls wrapped by this Expression.
   */
  final List<Call> calls = []

  /**
   * The type of this Expression (CALL_CHAIN, MAP, ...)
   */
  final ExpressionType type

  /**
   * The syntactical Role of the Expression
   */
  ExpressionRole role

  /**
   * Factory method.
   * Creates an Expression which wraps a whole sentence.
   * @return an Expression with role SENTENCE_ROOT, type
   * CALL_CHAIN and no parent
   */
  static Expression sentenceRoot() {
    new Expression (ExpressionType.CALL_CHAIN, ExpressionRole.SENTENCE_ROOT)
  }

  /**
   * Factory Method.
   * Creates an Expression to be used as a method param.
   * @return an Expression with role METHOD_PARAM, type
   * CALL_CHAIN and no parent
   */
  static Expression methodParamCallChain() {
    new Expression (ExpressionType.CALL_CHAIN, ExpressionRole.METHOD_PARAM)
  }

  /**
   * Factory Method.
   * Creates an Expression of Type Map to be used as a
   * method param.
   * @return an Expression with role METHOD_PARAM, type
   * MAP and no parent
   */
  static Expression methodParamMap() {
    new Expression (ExpressionType.MAP, ExpressionRole.METHOD_PARAM)
  }

  /**
   * Creates a new Expression with the specified properties.
   * Usually it's preferable to use one of the static
   * factory methods.
   * @param type the type of this Expression (CALL_CHAIN, MAP, ...)
   * @param role the syntactical Role of the Expression
   */
  private Expression(ExpressionType type, ExpressionRole role) {
    this.type = type
    this.role = role
  }

  /**
   * Creates a new Expression with the specified properties.
   * Usually it's preferable to use one of the static
   * factory methods.
   * @param calls the Calls to be wrapped by this Expression
   * @param type the type of this Expression (CALL_CHAIN, MAP, ...)
   * @param role the syntactical Role of the Expression
   */
  Expression(List<Call> calls, ExpressionType type, ExpressionRole role) {
    this(type, role)
    this.calls += calls
  }

  /**
   * creates a textual interpretation of this Expression
   * and it's embedded Calls. Only words will be returned
   * (i.e. interpunction is omitted).
   * @return a List containing one String for each word
   * @return
   */
  List<String> getWords() {
    calls.collect {
      it.words
    }.flatten()
    // TODO: optimize with collectMany() or whatnot
  }

}
