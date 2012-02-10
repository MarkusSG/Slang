package de.keelin.slang.closurerecorder

/**
 * Date: 02.02.12
 * Time: 07:37
 */
protected class RecordingDelegate implements GroovyInterceptable {

  final Recording recording

  RecordingDelegate(Recording recording) {
    this.recording = recording
  }

  @Override
  def invokeMethod(String name, args) {
    def result = recording.recordMethodCall(name, [*args])
    if (result != null)
      return result
    else
      return this
  }

  @Override
  def getProperty(String name) {
    def result = recording.recordPropertyRead(name)
    if (result != null)
      return result
    else
      return this
  }
}
