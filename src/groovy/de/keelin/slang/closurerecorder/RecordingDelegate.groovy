package de.keelin.slang.closurerecorder

/**
 * Date: 02.02.12
 * Time: 07:37
 */
class RecordingDelegate implements GroovyInterceptable {

  final Recording recording

  RecordingDelegate(Recording recording) {
    this.recording = recording
  }

  @Override
  def invokeMethod(String name, args) {
    recording.recordMethodCall(name, [*args])
  }

  @Override
  def getProperty(String name) {
    recording.recordPropertyRead(name)
  }
}
