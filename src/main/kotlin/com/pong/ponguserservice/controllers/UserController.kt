package com.pong.ponguserservice.controllers

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController {
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello world!"
    }
}