package com.robiumautomations.polyhex.security.exceptions

import org.springframework.security.core.AuthenticationException

class JwtTokenMissingException(message: String) : AuthenticationException(message)