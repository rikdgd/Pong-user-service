package com.pong.ponguserservice.repository

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.pong.ponguserservice.models.UserModel
import com.pong.ponguserservice.utils.MongodbHelper
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import kotlinx.coroutines.runBlocking



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


    /**
     * Gets a user from the database by its ObjectId.
     *
     * @param id The stringified ObjectId of the user
     * @return The UserModel if found, null if unsuccessful
     */
    suspend fun getUserById(id: String): UserModel? = runBlocking {
        val tools = getMongoTools();
        val objId = ObjectId(id)

        val resultsFlow = tools.collection.withDocumentClass<UserModel>()
            .find(eq("_id", objId))
            .firstOrNull()

        tools.client.close()
        resultsFlow
    }


    /**
     * Creates a new user in the database.
     *
     * @param username Username for the new user
     * @param password Password for the new user
     * @return The ObjectId of the new user, null if unsuccessful.
     */
    suspend fun createUser(username: String, password: String): ObjectId? {
        val mongoTools = getMongoTools()
        val mongodbHelper = MongodbHelper()
        var newUserId: ObjectId? = null

        try {
            val result = mongoTools.collection.insertOne(
                UserModel(ObjectId(), username, password)
            )
            newUserId = mongodbHelper.parseBsonValueToObjectId(result.insertedId)
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }

        mongoTools.client.close()
        return newUserId
    }


    /**
     * Creates a new user in the database.
     *
     * @param user Userdata for the new user.
     * @return The ObjectId of the new user, null if unsuccessful.
     */
    suspend fun createUser(user: UserModel): ObjectId? {
        val mongoTools = getMongoTools()
        val mongodbHelper = MongodbHelper()
        var newUserId: ObjectId? = null

        try {
            val result = mongoTools.collection.insertOne(
                UserModel(ObjectId(), user.name, user.password)
            )
            newUserId = mongodbHelper.parseBsonValueToObjectId(result.insertedId)
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }

        mongoTools.client.close()
        return newUserId
    }


    /**
     * Updates a user in the database with the provided UserModel.
     *
     * @param updatedUser UserModel with the new data
     * @return true if the update was successful, false otherwise
     */
    suspend fun updateUser(updatedUser: UserModel): Boolean {
        val mongoTools = getMongoTools()
        val objId = ObjectId(updatedUser.id.toString())

        val query = Filters.eq("_id", objId)
        val updates = Updates.combine(
            Updates.set(UserModel::name.name, updatedUser.name),
            Updates.set(UserModel::password.name, updatedUser.password),
        )

        // If the user doesn't exist yet, don't create it.
        val options = UpdateOptions().upsert(false)

        try {
            val result = mongoTools.collection.updateOne(query, updates, options)
            println("Modified document count: " + result.modifiedCount)
            println("Upserted id: " + result.upsertedId) // only contains a non-null value when an upsert is performed
            return true
        } catch (e: MongoException) {
            System.err.println("Unable to update due to an error: $e")
            return false
        } finally {
            mongoTools.client.close()
        }
    }
}

private data class MongoTools(
    val client: MongoClient,
    val database: MongoDatabase,
    val collection: MongoCollection<UserModel>
)
