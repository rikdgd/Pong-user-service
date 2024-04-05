package com.pong.ponguserservice.utils

import org.bson.BsonValue
import org.bson.types.ObjectId

class MongodbHelper {

    fun parseBsonValueToObjectId(bsonValue: BsonValue?): ObjectId? {
        return try {
            ObjectId(bsonValue?.asObjectId()?.value?.toHexString())
        } catch (e: Exception) {
            println("Failed to convert BsonValue into an ObjectId: $e")
            null
        }
    }
}