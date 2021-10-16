package grpc.client

import grpc.logger.Logger
import grpc.proto.Server
import grpc.proto.UserServiceGrpcKt
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() {
    val channel =
        ManagedChannelBuilder
            .forAddress(Config.host, Config.port)
            .usePlaintext() // necessary without TLS
            .build()

    val client = UserServiceGrpcKt.UserServiceCoroutineStub(channel)

    runBlocking {
        try {
            client.createManyUsers()
        }catch (e: Exception) {
            Logger.error(e.cause)
        }
    }
}

@OptIn(ExperimentalTime::class)
fun UserServiceGrpcKt.UserServiceCoroutineStub.createManyUsers() {
    val n = 10_000
    measureTime {
        runBlocking {
            sendSaveUserConcurrent(this, n)
        }
    }.also { println("created $n users in ${it.inWholeMilliseconds}s") }
}

suspend fun UserServiceGrpcKt.UserServiceCoroutineStub.sendSaveUser(email: String) {
    val response = saveUser(
        Server.SaveUserRequest.newBuilder()
            .setEmail(email)
            .build()
    )

    Logger.debug("created user ${response.id}")
}

suspend fun UserServiceGrpcKt.UserServiceCoroutineStub.sendSaveUserConcurrent(scope: CoroutineScope, n: Int) {
    repeat(n) { i ->
        scope.launch {
            sendSaveUser(UUID.randomUUID().toString())
        }
    }
}

object Config {
    const val host = "localhost"
    const val port = 8080
}
