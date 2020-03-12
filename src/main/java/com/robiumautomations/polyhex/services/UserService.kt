package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.models.User
import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.dtos.users.RegistrationUser
import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.repos.UserCredentialsRepo
import com.robiumautomations.polyhex.repos.UserRepo
import com.robiumautomations.polyhex.security.AuthenticationUser
import com.robiumautomations.polyhex.security.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
open class UserService : UserDetailsService {

  @Autowired
  private lateinit var userCredentialsRepo: UserCredentialsRepo
  @Autowired
  private lateinit var userRepo: UserRepo

  override fun loadUserByUsername(username: String): UserDetails? {
    return userCredentialsRepo.getByUsername(username)?.let {
      AuthenticationUser(
          userId = it.userId!!,
          password = it.password!!,
          username = it.username!!,
          role = it.userRole!!,
          authorities = emptyList()
      )
    }
  }

  fun getUserInfo(userId: UserId): User? {
    return userRepo.findByIdOrNull(userId)
  }

  open fun registerNewUser(registrationUser: RegistrationUser): User {
    validateInputs(registrationUser)
    val userId = UUID.randomUUID().toString()
    UserCredentials(
        userId = userId,
        username = registrationUser.username,
        email = registrationUser.email,
        password = JwtUtils.hashPassword(registrationUser.password!!),
        userRole = UserRole.user
    ).also {
      userCredentialsRepo.save(it)
    }

    return User(
        userId = userId,
        username = registrationUser.username!!,
        firstName = registrationUser.firstName,
        lastName = registrationUser.lastName,
        birthday = registrationUser.birthday
    ).also {
      save(it)
    }
  }

  private fun validateInputs(registrationUser: RegistrationUser) {
    if (registrationUser.username.isNullOrBlank()) {
      throw Exception("Username is not set.")
    }
    if (registrationUser.password.isNullOrBlank()) {
      throw Exception("Password is not set.")
    }
    if (!isUsernameFree(registrationUser.username)) {
      throw Exception("Username is already in use.")
    }
    registrationUser.email?.let {
      if (!isEmailFree(it)) {
        throw Exception("Email is already in use.")
      }
    }
    if (!isValidUsername(registrationUser.username)) {
      throw Exception("Username is not valid.")
    }
  }

  fun isEmailFree(email: String): Boolean {
    return userCredentialsRepo.getIdByEmail(email) == null
  }

  fun isUsernameFree(username: String): Boolean {
    return userCredentialsRepo.getIdByUsername(username) == null
  }

  private fun isValidUsername(username: String): Boolean {
    return """^(?=.{8,20}${'$'})(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])${'$'}""".toRegex().matches(username)
  }

  fun save(user: User) {
    userRepo.save(user)
  }
}