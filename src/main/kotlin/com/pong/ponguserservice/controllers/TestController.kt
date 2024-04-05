package com.pong.ponguserservice.controllers

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/test")
class TestController {

    private final var connectionString = ""

    init {
        val connectionStringFromEnv = System.getenv("MONGODB_URI")
        if (!connectionStringFromEnv.isNullOrEmpty()) {
            this.connectionString = connectionStringFromEnv
        } else {
            throw IllegalStateException("Database connection string not found in environment variables")
        }
    }

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello world!")
    }

    @GetMapping("/pingMongodb")
    fun pingMongodb(): ResponseEntity<String> {

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

                return ResponseEntity.ok("Pinged your deployment. You successfully connected to MongoDB!")
            }
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            return ResponseEntity.badRequest().body("Failed to ping database: $errorMessage")
        }
    }
}
