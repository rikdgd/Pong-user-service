package com.pong.ponguserservice.controllers

import com.mongodb.*
import org.springframework.web.bind.annotation.*
import org.bson.Document
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.pong.ponguserservice.models.UserModel
import com.pong.ponguserservice.repository.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bson.BsonValue
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/users")
class UserController() {

    private final val userRepository: UserRepository = UserRepository()


    @GetMapping("/getById")
    fun getUserById(@RequestParam id: ObjectId): ResponseEntity<UserModel> {
        var result: UserModel?

        runBlocking {
            result = userRepository.getUserById(id)
        }
        result ?: return ResponseEntity.internalServerError().body(null)

        return ResponseEntity.ok(result)
    }


    @PostMapping("/createUser")
    fun createUser(@RequestParam username: String, @RequestParam password: String): ResponseEntity<ObjectId> {
        var newId: ObjectId?

        runBlocking {
            newId = userRepository.createUser(username, password)
        }
        newId ?: return ResponseEntity.internalServerError().body(null)

        return ResponseEntity.ok(newId)
    }


    @PostMapping("/editUser")
    fun editUser(@RequestParam id: ObjectId): ResponseEntity<UserModel> {
        TODO()
    }


    @PostMapping("/removeById")
    fun removeUserById(@RequestParam id: ObjectId): ResponseEntity<UserModel> {
        TODO()
    }
}
