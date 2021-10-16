package grpc.logger

object Logger {

    private const val isDebugEnabled = false

    fun info(msg: Any?) {
        println("INFO: $msg")
    }

    fun error(msg: Any?) {
        println("ERROR: $msg")
    }

    fun debug(msg: Any) {
        if (isDebugEnabled) println("DEBUG: $msg")
    }
}
