package grpc.server

import grpc.proto.Server
import grpc.proto.UserServiceGrpcKt

class UserService: UserServiceGrpcKt.UserServiceCoroutineImplBase() {

    private var currentID = 0

    override suspend fun saveUser(request: Server.SaveUserRequest): Server.SaveUserResponse {
        return Server.SaveUserResponse.newBuilder()
            .setId(currentID++.toString())
            .build()
    }
}