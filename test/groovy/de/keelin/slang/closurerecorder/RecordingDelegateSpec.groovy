package de.keelin.slang.closurerecorder

import spock.lang.Specification

/**
 * Date: 02.02.12
 * Time: 12:51
 */
class RecordingDelegateSpec extends Specification {

  ExpressionRecording recording = Mock()
  RecordingDelegate delegate = new RecordingDelegate(recording)

  def "delegate will relay some arbitrary method call to the wrapped ExpressionRecording" () {
    when : "some method is called on the delegate with arbitrary parameters"
    delegate.someMethod(with:3, "arbitrary", "parameters")
    then : "recordMethodCall() will be called with the corresponding parameters on the recording"
    1 * recording.recordMethodCall("someMethod", [[with:3], "arbitrary", "parameters"])
  }

  def "delegate will relay some arbitrary property read to the wrapped ExpressionRecording" () {
    when : "some property is read on the delegate"
    delegate.someProperty
    then : "recordPropertyRead() will be called with the name of the requested property as parameter"
    1 * recording.recordPropertyRead("someProperty")
  }

}
