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
    expect:
    call.words == expectedWords
    where:
    call                                                         | expectedWords
    objectRef("text", null)                                      | ["text"]
    methodWithParams("method", "param1", "param2")               | ["method", "param1", "param2"]
    methodWithParams("method", [key1: "param1", key2: "param2"]) | ["method", "key1", "param1", "key2", "param2"]
  }

  def "mapEntry() creates a map-entry Call" () {
    given: "a Map Expression as parent for the map-entry Call"
    Expression parent = Expression.methodParamMap(null)
    and: "some Expression as value for the map entry"
    Expression value = Expression.sentenceRoot()
    when: "mapEntry is called"
    CallWithSubexpressions result = Call.mapEntry("key", value, parent)
    then: "the result has the correct structure of a map-entry Expression"
    result.parent == parent
    result.value == "key"
    result.subexpressions.size() == 1
    result.subexpressions[0] == value
    result.subexpressions[0].parent == result
  }

  static Call methodWithParams(String name, Object... params) {
    CallWithSubexpressions result = method(name, null)
    params.each {
      if (it instanceof Map) {
        Expression ex = methodParamMap(null)
        it.each {key, value ->
          ex.calls << Call.objectRef(key, ex)
          ex.calls << Call.objectRef(value, ex)
        }
        result.subexpressions << ex
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
