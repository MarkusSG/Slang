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

  def "recordMethodCall() will return itself if the recording returns nothing" () {
    given: "the recording returns null for recordMethodCall()"
    recording.recordMethodCall("someMethod", []) >> null
    when : "some method gets called on the delegate"
    def result = delegate.someMethod()
    then : "the result will be the delegate itself"
    result == delegate
  }

  def "recordPropertyRead will return itself if the recording returns nothing" () {
    given: "the recording returns null for recordMethodCall()"
    recording.recordPropertyRead("someProp") >> null
    when : "some property gets asked from the delegate"
    def result = delegate.someProp
    then : "the result will be the delegate itself"
    result == delegate
  }

}
