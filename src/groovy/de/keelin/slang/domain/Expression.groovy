package de.keelin.slang.domain

/**
 * Date: 31.01.12
 * Time: 12:43
 */
class Expression {

  final Call parent
  final List<Call> calls = []
  final ExpressionType type
  ExpressionRole role

  static Expression sentenceRoot() {
    new Expression (null, ExpressionType.CALL_CHAIN, ExpressionRole.SENTENCE_ROOT)
  }

  static Expression methodParamCallChain(Call parent) {
    new Expression (parent, ExpressionType.CALL_CHAIN, ExpressionRole.METHOD_PARAM)
  }

  static Expression methodParamMap(Call parent) {
    new Expression (parent, ExpressionType.MAP, ExpressionRole.METHOD_PARAM)
  }

  private Expression(Call parent, ExpressionType type, ExpressionRole role) {
    this.parent = parent
    this.type = type
    this.role = role
  }

  Expression(Call parent, List<Call> calls, ExpressionType type, ExpressionRole role) {
    this(parent, type, role)
    this.calls += calls
  }

  List<String> getWords() {
    calls.collect {
      it.words
    }.flatten()
    // TODO: optimize with collectMany() or whatnot
  }

}
