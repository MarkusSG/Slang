package de.keelin.slang.domain

import static Expression.*
import static de.keelin.slang.domain.Call.*

/**
 * Date: 01.02.12
 * Time: 11:32
 */
class ExpressionTestUtil {

  static Expression propertyRead(String name) {
    Expression expression = sentenceRoot()
    expression.calls << propertyRead(name, expression)
    expression
  }
}
