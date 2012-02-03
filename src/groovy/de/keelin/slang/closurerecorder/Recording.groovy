package de.keelin.slang.closurerecorder

/**
 * Date: 03.02.12
 * Time: 17:26
 */
interface Recording {

  def recordMethodCall(final String name, final List args)

  def recordPropertyRead(final String name)

}
