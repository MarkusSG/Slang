package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.Call
import de.keelin.slang.domain.CallWithSubexpressions

/**
 * Date: 31.01.12
 * Time: 16:10
 */
class SentenceRecording {


  Expression rootExpression

  int getSize() {
    getWords().size()
  }

  List<String> getWords() {
    rootExpression.words
  }

  def recordMethodCall(String name, List params) {
    if (!rootExpression) {
      rootExpression = Expression.methodParamCallChain(null)
    }
    CallWithSubexpressions method = Call.method(name,rootExpression)
    rootExpression.calls << method
    method.subexpressions.addAll(convertParams(params, method))
  }

  List<Expression> convertParams(params, Call parent) {
    params.collect {
      Expression ex = Expression.methodParamCallChain(parent)
      ex.calls << Call.objectRef(it, ex)
      ex
    }
  }
}
