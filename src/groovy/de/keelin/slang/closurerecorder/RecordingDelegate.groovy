package de.keelin.slang.closurerecorder

/**
 * Instances of this class serve as generic
 * interceptors and delegators on Closure
 * and Expression level.
 * Best used as Closure delegate or return
 * value from intercepted method calls.
 *
 * Cuurently, the interceptor methods return
 * this if the delegate returns null. This
 * will most probably be changed in the future.
 *
 * @author Markus Günther
 */
protected class RecordingDelegate implements GroovyInterceptable {

  /**
   * The Recording to which this delegates
   */
  final Recording recording

  /**
   * @param recording The Recording to which this shall delegate
   */
  RecordingDelegate(Recording recording) {
    this.recording = recording
  }

  /**
   * intercepts all method calls directed
   * at this and delegates them to
   * {@link Recording#recordMethodCall(String, List)}.
   * @param name the name of the method
   * @param args the parameters
   * @return the result returned by the delegate or
   * this (if that result was null)
   */
  @Override
  def invokeMethod(String name, args) {
    def result = recording.recordMethodCall(name, [*args])
    if (result != null)
      return result
    else
      return this
  }

  /**
   * intercepts all property reads directed
   * at this and delegates them to
   * {@link Recording#recordMethodCall(String, List)}.
   * @param name the name of the method
   * @return the result returned by the delegate or
   * this (if that result was null)
   */
  @Override
  def getProperty(String name) {
    def result = recording.recordPropertyRead(name)
    if (result != null)
      return result
    else
      return this
  }
}
