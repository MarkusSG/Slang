package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.Expression
import de.keelin.slang.domain.Call
import de.keelin.slang.domain.CallWithSubexpressions

import static de.keelin.slang.domain.Expression.*
import static de.keelin.slang.domain.Call.*
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
    initRootExpression()
    CallWithSubexpressions method = method(name,rootExpression)
    rootExpression.calls << method
    method.subexpressions.addAll(convertParams(params, method))
  }

  def recordPropertyRead(String name) {
    initRootExpression()
    rootExpression.calls << propertyRead(name, rootExpression)
  }

  private void initRootExpression() {
    if (!rootExpression) {
      rootExpression = methodParamCallChain(null)
    }
  }

  private List<Expression> convertParams(params, Call parent) {
    params.collect {
      Expression ex = methodParamCallChain(parent)
      ex.calls << Call.objectRef(it, ex)
      ex
    }
  }

}
