package de.keelin.slang.matcher

/**
 * Matcher for exactly one String word
 */
class ExactWordMatcher {

  final String word

  ExactWordMatcher(String word) {
    this.word = word
  }

  boolean match(String word) {
    this.word == word
  }
}
