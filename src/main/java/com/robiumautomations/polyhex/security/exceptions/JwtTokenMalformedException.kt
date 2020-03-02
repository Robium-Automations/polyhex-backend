package com.robiumautomations.polyhex.security.exceptions

import org.springframework.security.core.AuthenticationException

class JwtTokenMalformedException(message: String): AuthenticationException(message)