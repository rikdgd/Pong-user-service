package com.pong.ponguserservice.controllers

import org.springframework.web.bind.annotation.*
import com.pong.ponguserservice.models.UserModel
import com.pong.ponguserservice.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity



@RestController
@RequestMapping("/users")
class UserController() {

    private final val userRepository: UserRepository = UserRepository()


    @GetMapping("/getById")
    fun getUserById(@RequestParam id: String): ResponseEntity<UserModel> {
        var result: UserModel?

        runBlocking {
            result = userRepository.getUserById(ObjectId(id))
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
