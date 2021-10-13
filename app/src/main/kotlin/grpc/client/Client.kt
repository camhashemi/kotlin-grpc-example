package grpc.client

import grpc.proto.Server
import grpc.proto.UserServiceGrpcKt
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking

fun main() {
    val channel =
        ManagedChannelBuilder
            .forAddress(Config.host, Config.port)
            .usePlaintext() // necessary without TLS
            .build()

    val client = UserServiceGrpcKt.UserServiceCoroutineStub(channel)

    runBlocking {
        val response = client.saveUser(
            Server.SaveUserRequest.newBuilder()
                .setEmail("a@b.c")
                .build()
        )

        println("INFO: created user ${response.id}")
    }
}

object Config {
    const val host = "localhost"
    const val port = 8080
}