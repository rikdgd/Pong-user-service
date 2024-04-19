package com.pong.ponguserservice

import org.junit.Test;
import org.junit.Assert.*

class PongUserServiceApplicationTests {

	@Test
	fun addOneTest() {
		val number = 5

		assertEquals(addOne(5), 6)
	}

	private fun addOne(n: Int): Int {
		return n + 1
	}
}