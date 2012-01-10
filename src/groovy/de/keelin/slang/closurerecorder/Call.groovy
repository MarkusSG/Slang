package de.keelin.slang.closurerecorder

import groovy.transform.Canonical

/**
 * Date: 10.01.12
 * Time: 17:05
 */
@Canonical
class Call {
  enum Type {
    READ_PROPERTY, WRITE_PROPERTY, CALL_METHOD
  }

  final Type type
  final String name
  final List args

  Call(final Type type, final String name) {
    this.type = type
    this.name = name
    this.args = []
  }

  Call(final Type type, final String name, final List args) {
    this(type, name)
    this.args.addAll(args)
  }
}
