package com.pong.ponguserservice.controllers

import org.springframework.web.bind.annotation.*
import org.bson.Document
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking



@RestController
@RequestMapping("/api")
class UserController {
    val connectionString = "mongodb+srv://Rik:gJCVjXjYvVRPX1ZY@pong-dev.rv7wbli.mongodb.net/?retryWrites=true&w=majority&appName=Pong-dev"

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
            .applyConnectionString(ConnectionString(connectionString))
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
}