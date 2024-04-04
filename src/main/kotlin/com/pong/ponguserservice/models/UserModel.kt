package com.pong.ponguserservice.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserModel(
    @BsonId val id: ObjectId,
    val name: String,
    val password: String
)

