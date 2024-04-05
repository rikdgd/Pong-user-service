package com.pong.ponguserservice.controllers

import com.mongodb.*
import org.springframework.web.bind.annotation.*
import org.bson.Document
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.pong.ponguserservice.models.UserModel
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId


@RestController
@RequestMapping("/users")
class UserController {
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
