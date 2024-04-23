package com.pong.ponguserservice.controllers

import org.springframework.web.bind.annotation.*
import com.pong.ponguserservice.models.UserModel
import com.pong.ponguserservice.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException.BadRequest


@RestController
@CrossOrigin
@RequestMapping("/users")
class UserController() {

    private final val userRepository: UserRepository = UserRepository()


    @GetMapping("/getById")
    fun getUserById(@RequestParam id: String): ResponseEntity<UserModel> {
        var result: UserModel?

        runBlocking {
            result = userRepository.getUserById(id)
        }

        result ?: return ResponseEntity.internalServerError().body(null)

        return ResponseEntity.ok(result)
    }

    @PostMapping("/createUser")
    fun createUser(@RequestBody user: UserModel): ResponseEntity<ObjectId> {
        var newId: ObjectId?

        runBlocking {
            newId = userRepository.createUser(user)
        }
        newId ?: return ResponseEntity.internalServerError().body(null)

        return ResponseEntity.ok(newId)
    }


    /**
     * Updates a user by its ObjectId in the database.
     *
     * @param user UserModel with the new data and id of the target user
     * @return true if the update was successful, false otherwise
     */
    @PostMapping("/editUser")
    fun editUser(@RequestBody user: UserModel): ResponseEntity<Boolean> {
        user.id ?: return ResponseEntity.badRequest().body(null)

        val responseEntity = runBlocking {
            val result = userRepository.updateUser(user)

            if (result) {
                ResponseEntity.ok(true)
            } else {
                ResponseEntity.internalServerError().body(false)
            }
        }
        return responseEntity
    }


    @PostMapping("/removeById")
    fun removeUserById(@RequestParam id: ObjectId): ResponseEntity<UserModel> {
        TODO()
    }
}
