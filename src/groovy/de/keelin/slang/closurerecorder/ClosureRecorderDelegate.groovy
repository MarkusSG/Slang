package de.keelin.slang.closurerecorder

/**
 * Date: 10.01.12
 * Time: 17:01
 */
class ClosureRecorderDelegate implements GroovyInterceptable {

    final List calls

    ClosureRecorderDelegate(List calls) {
      this.calls = calls
    }

    @Override
    def invokeMethod(String name, args) {
      // This slightly complicated code is necessary,
      // because accessing calls directly from the collect-closure
      // would result in a getProperty()-call
      def callsToBeRemoved = []
      def filteredArgs = args.collect { arg ->
        if (arg instanceof Call) {
          callsToBeRemoved << arg
          arg.name
        } else {
          arg
        }
      }
      calls.removeAll(callsToBeRemoved)
      calls << new Call(Call.Type.CALL_METHOD, name, filteredArgs)
      return this
    }

    @Override
    def getProperty(String name) {
      Call call = new Call(Call.Type.READ_PROPERTY, name)
      calls << call
      return call
    }
}
