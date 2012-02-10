package de.keelin.slang.closurerecorder

/**
 * Interface for classes storing recorded calls.
 *
 * @author Markus Günther
 */
protected interface Recording {

  /**
   * Records the specified method call.
   * @param name the name of the method
   * @param args the argument passed to the method
   * @return depends on the implementation
   */

  def recordMethodCall(final String name, final List args)

  /**
   * Records the specified property access
   * @param name the name of the property
   * @return depends on the implementation
   */
  def recordPropertyRead(final String name)

  //TODO: def recordPropertyWrite(final String name, final value)


}
