package de.keelin.slang.domain

import spock.lang.Specification
import static Expression.*
import static Call.*
/**
 * Date: 31.01.12
 * Time: 16:38
 */
class CallSpec extends Specification {


  def "Call.words() returns a List of Strings representing the Call and it's subexpresions" () {
    if (call instanceof CallWithSubexpressions && subexpressions) {
      call.subexpressions += subexpressions
    }
    expect:
    call.words == expectedWords
    where:
    call                                           | subexpressions | expectedWords
    objectRef("text", null)                        | null           | ["text"]
    methodWithParams("method", "param1", "param2") | null           | ["method", "param1", "param2"]
  }

  static Call methodWithParams(String name, Object... params) {
    CallWithSubexpressions result = method(name, null)
    params.each {
      if (it instanceof Map) {
        // TODO
      } else if (it instanceof Expression) {
        result.subexpressions << it
      } else {
        Expression ex = methodParamCallChain(null)
        ex.calls << Call.objectRef(it, ex)
        result.subexpressions << ex
      }
    }
    result
  }

}
