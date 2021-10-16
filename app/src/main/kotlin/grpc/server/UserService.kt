package grpc.server

import grpc.proto.Server
import grpc.proto.UserServiceGrpcKt
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

typealias ID = String
typealias Email = String

data class User(val id: ID, val email: Email)

class UserService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {

    private var currentID = 0
    private val users = mutableListOf<User>()
    private val lock = Mutex()

    override suspend fun saveUser(request: Server.SaveUserRequest): Server.SaveUserResponse {
        lock.withLock {
            if (users.any { it.email == request.email }) {
                throw Exception("user with email already exists")
            }

            val id = currentID++

            users.add(User(id.toString(), request.email))

            return Server.SaveUserResponse.newBuilder()
                .setId(id.toString())
                .build()
        }
    }
}