package com.pong.ponguserservice.controllers

import com.mongodb.*
import org.springframework.web.bind.annotation.*
import org.bson.Document
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.pong.ponguserservice.models.UserModel
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId


@RestController
@RequestMapping("/api")
class UserController() {
    private final var connectionString = ""
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

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello world!"
    }

    @GetMapping("/mongo")
    fun pingDb(): String {
        var result: String

        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()

        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(this.connectionString))
            .serverApi(serverApi)
            .build()

        try {
            MongoClient.create(mongoClientSettings).use { mongoClient ->
                val database = mongoClient.getDatabase("admin")
                runBlocking {
                    database.runCommand(Document("ping", 1))
                }
                result = "Pinged your deployment. You successfully connected to MongoDB!"
            }
        } catch (e: Exception) {
            result = e.message.toString()
        }

        return result
    }

    @PostMapping("/createUser")
    fun createUser(@RequestParam username: String, @RequestParam password: String) {
        val client = MongoClient.create(this.connectionString)
        val database = client.getDatabase(this.dbName)
        val collection = database.getCollection<UserModel>(this.collectionName)

        runBlocking {
            try {
                val result = collection.insertOne(
                    UserModel(ObjectId(), username, password)
                )
                println("Success! Inserted document id: " + result.insertedId)
            } catch (e: MongoException) {
                System.err.println("Unable to insert due to an error: $e")
            }
        }

        client.close()
    }
}
