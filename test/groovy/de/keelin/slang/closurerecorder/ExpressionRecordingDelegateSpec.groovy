package de.keelin.slang.closurerecorder

import spock.lang.Specification

/**
 * Date: 02.02.12
 * Time: 12:51
 */
class ExpressionRecordingDelegateSpec extends Specification {

  ExpressionRecording recording = Mock()
  ExpressionRecordingDelegate delegate = new ExpressionRecordingDelegate(recording)

  def "delegate will relay some arbitrary method call to the wrapped ExpressionRecording" () {
    when : "some method is called on the delegate with arbitrary parameters"
    delegate.someMethod(with:3, "arbitrary", "parameters")
    then : "recordMethodCall() will be called with the corresponding parameters on the recording"
    1 * recording.recordMethodCall("someMethod", [[with:3], "arbitrary", "parameters"])
  }

}
