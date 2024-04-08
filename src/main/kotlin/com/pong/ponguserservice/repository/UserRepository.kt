package com.pong.ponguserservice.repository

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.pong.ponguserservice.models.UserModel
import com.pong.ponguserservice.utils.MongodbHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
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


    suspend fun getUserById(id: ObjectId): UserModel? {
        val mongoTools = getMongoTools()
        var userModel: UserModel? = null

        coroutineScope {
            val job = launch {
                try {
                    val result = mongoTools.collection.find(
                        Filters.eq(UserModel::id.name, id)
                    ).firstOrNull()

                    result?.let { user ->
                        userModel = user
                    }

                } catch (ex: Exception) {
                    println("Failed to get user data by given id")
                }
            }
        }

        return userModel
    }


    fun createUser(username: String, password: String): ObjectId? {
        val mongoTools = getMongoTools()
        val mongodbHelper = MongodbHelper()
        var newUserId: ObjectId? = null

        runBlocking {
            try {
                val result = mongoTools.collection.insertOne(
                    UserModel(ObjectId(), username, password)
                )
                newUserId = mongodbHelper.parseBsonValueToObjectId(result.insertedId)
            } catch (e: MongoException) {
                System.err.println("Unable to insert due to an error: $e")
            }
        }

        mongoTools.client.close()
        return newUserId
    }
}

private data class MongoTools(
    val client: MongoClient,
    val database: MongoDatabase,
    val collection: MongoCollection<UserModel>
)
