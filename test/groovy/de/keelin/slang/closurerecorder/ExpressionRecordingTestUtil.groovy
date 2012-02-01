package de.keelin.slang.closurerecorder

import de.keelin.slang.domain.ExpressionTestUtil

/**
 * Date: 01.02.12
 * Time: 11:27
 */
class ExpressionRecordingTestUtil {

  static ExpressionRecording propertyRead(String name) {
    new ExpressionRecording(ExpressionTestUtil.propertyRead(name))
  }

  static ExpressionRecording registeredPropertyRead(String name, DelegatePropertyRegistry registry) {
    def propertyExpression = ExpressionTestUtil.propertyRead(name)
    ExpressionRecording recording = new ExpressionRecording(propertyExpression)
    registry.add(propertyExpression, recording)
    recording
  }

}
