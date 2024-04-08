package com.pong.ponguserservice.models

import org.bson.BsonDocument
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

class UserModel {
    @BsonId
    val id: ObjectId
    val name: String
    val password: String

    constructor(id: ObjectId, name: String, password: String) {
        this.id = id
        this.name = name
        this.password = password
    }

    constructor(document: BsonDocument) {
        val id = document.getObjectId("_id").value?: throw IllegalArgumentException("Missing _id field")
        val name = document.getString("name")?: throw IllegalArgumentException("Missing name field")
        val password = document.getString("password")?: throw IllegalArgumentException("Missing password field")

        this.id = id
        this.name = name.toString()
        this.password = password.toString()
    }
}
