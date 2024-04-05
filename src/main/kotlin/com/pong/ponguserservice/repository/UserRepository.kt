package com.pong.ponguserservice.repository

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.pong.ponguserservice.models.UserModel
import kotlinx.coroutines.runBlocking
import org.bson.BsonValue
import org.bson.types.ObjectId

class UserRepository {

    private var connectionString = ""
    private val dbName = "development"
    private val collectionName = "users"

    init {
        val connectionStringFromEnv = System.getenv("MONGODB_URI")
        if (!connectionStringFromEnv.isNullOrEmpty()) {
            this.connectionString = connectionStringFromEnv
        } else {
            throw IllegalStateException("Database connection string not found in environment variables")
        }
    }

    private fun getMongoTools(): MongoTools {
        val client = MongoClient.create(this.connectionString)
        val database = client.getDatabase(this.dbName)
        val collection = database.getCollection<UserModel>(this.collectionName)

        return MongoTools(client, database, collection)
    }


    fun createUser(username: String, password: String): BsonValue? {
        val mongoTools = getMongoTools()
        var newUserId: BsonValue? = null

        runBlocking {
            try {
                val result = mongoTools.collection.insertOne(
                    UserModel(ObjectId(), username, password)
                )
                newUserId = result.insertedId
            } catch (e: MongoException) {
                System.err.println("Unable to insert due to an error: $e")
            }
        }

        mongoTools.client.close()
        return newUserId
    }
}

private class MongoTools(
    val client: MongoClient,
    val database: MongoDatabase,
    val collection: MongoCollection<UserModel>
)
