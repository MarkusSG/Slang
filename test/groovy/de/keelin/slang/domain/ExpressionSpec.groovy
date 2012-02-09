package de.keelin.slang.domain

import static Expression.*
import static Call.*
import spock.lang.Specification

/**
 * Date: 31.01.12
 * Time: 16:52
 */
class ExpressionSpec extends Specification {

  def "Expression.words() returns a List of Strings representing the Expression's calls" () {
    expect:
    ex.words == expectedWords
    where:
    ex                                               | expectedWords
    methodParamCallChainWithOneObjectRefCall("text") | ["text"]
  }

  static Expression methodParamCallChainWithOneObjectRefCall(ref) {
    Expression result = methodParamCallChain()
    result.calls << objectRef(ref)
    result
  }
}
