package com.robiumautomations.polyhex.utils

import com.robiumautomations.polyhex.security.utils.JwtUtils
import org.junit.jupiter.api.Test

class JwtUtilsTest {

  @Test
  fun `create hash password test`() {
    println(JwtUtils.hashPassword("password"))
  }
}