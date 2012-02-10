package de.keelin.slang.domain

/**
 * Describes the invocation background of a {@link Call}.
 *
 * @author Markus Günther
 */
enum CallOrigin {
  // any call in a call chain, except the first
  PREDECESSOR,
  // the first call in every call chain,
  // a property name used as method
  // parameter or assignment value
  DELEGATE,
  // any pogo
  NONE
}
