package de.keelin.slang.domain

/**
 * Date: 31.01.12
 * Time: 12:43
 */
class Expression {

  Call parent
  final List<Call> calls = []
  final ExpressionType type
  ExpressionRole role

  static Expression sentenceRoot() {
    new Expression (ExpressionType.CALL_CHAIN, ExpressionRole.SENTENCE_ROOT)
  }

  static Expression methodParamCallChain() {
    new Expression (ExpressionType.CALL_CHAIN, ExpressionRole.METHOD_PARAM)
  }

  static Expression methodParamMap() {
    new Expression (ExpressionType.MAP, ExpressionRole.METHOD_PARAM)
  }

  private Expression(ExpressionType type, ExpressionRole role) {
    this.type = type
    this.role = role
  }

  Expression(List<Call> calls, ExpressionType type, ExpressionRole role) {
    this(type, role)
    this.calls += calls
  }

  List<String> getWords() {
    calls.collect {
      it.words
    }.flatten()
    // TODO: optimize with collectMany() or whatnot
  }

}
