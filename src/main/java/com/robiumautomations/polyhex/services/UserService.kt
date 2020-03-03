package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.repos.UserRepo
import com.robiumautomations.polyhex.security.AuthenticationUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {

  @Autowired
  private lateinit var userRepo: UserRepo

  override fun loadUserByUsername(username: String): UserDetails? {
    return userRepo.getByUsername(username)?.let {
      AuthenticationUser(
          userId = it.userId!!,
          password = it.password!!,
          username = it.username!!,
          role = it.userRole!!,
          authorities = emptyList()
      )
    }
  }
}