package de.keelin.slang.matcher

import spock.lang.Specification

/**
 * Date: 18.02.12
 * Time: 15:49
 */
class ExactWordMatcherSpec extends Specification {

  ExactWordMatcher matcher = new ExactWordMatcher("word")

  def "Matcher matches to it's match value" () {
    when: "match() is called with the correct value"
    def result = matcher.match("word")
    then: "it returns true"
    result
  }

}
