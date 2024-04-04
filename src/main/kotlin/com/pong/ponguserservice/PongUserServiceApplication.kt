package com.pong.ponguserservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PongUserServiceApplication

fun main(args: Array<String>) {
	runApplication<PongUserServiceApplication>(*args)
}
