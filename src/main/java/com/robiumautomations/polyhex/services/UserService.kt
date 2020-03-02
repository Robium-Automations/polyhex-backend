package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.repos.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

  @Autowired
  private lateinit var userRepo: UserRepo

  fun getByUsername(username: String): UserCredentials? {
    return userRepo.getByUsername(username)
  }
}