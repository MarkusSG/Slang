package de.keelin.slang.closurerecorder

/**
 * Date: 02.02.12
 * Time: 07:37
 */
class ExpressionRecordingDelegate implements GroovyInterceptable {

  final ExpressionRecording recording

  ExpressionRecordingDelegate(ExpressionRecording recording) {
    this.recording = recording
  }

  @Override
  def invokeMethod(String name, args) {
    recording.recordMethodCall(name, [*args])
  }
}
