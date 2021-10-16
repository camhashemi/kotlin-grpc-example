package grpc.server

import grpc.logger.Logger
import io.grpc.ServerBuilder

fun main() {
    val server =
        ServerBuilder
            .forPort(Config.port)
            .addService(UserService())
            .intercept(LoggingInterceptor)
            .build()

    Logger.info("starting server on port ${Config.port}")

    server.start()

    Runtime.getRuntime()
        .addShutdownHook(
            Thread {
                Logger.info("shutting down server")
                server.shutdown()
                Logger.info("server shut down")
            }
        )

    server.awaitTermination()
}

object Config {
    const val port = 8080
}
