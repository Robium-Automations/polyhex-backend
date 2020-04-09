package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.models.User
import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.dtos.users.RegistrationUser
import com.robiumautomations.polyhex.dtos.users.UpdatedUser
import com.robiumautomations.polyhex.dtos.users.UserWithGroups
import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.repos.StudyGroupRepo
import com.robiumautomations.polyhex.repos.UserCredentialsRepo
import com.robiumautomations.polyhex.repos.UserRepo
import com.robiumautomations.polyhex.security.AuthenticationUser
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.security.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import java.util.*

@Service
open class UserService : UserDetailsService {

  @Autowired
  private lateinit var userCredentialsRepo: UserCredentialsRepo

  @Autowired
  private lateinit var userRepo: UserRepo

  @Autowired
  private lateinit var studyGroupRepo: StudyGroupRepo

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

  fun getUserWithGroups(userId: UserId): UserWithGroups? {
    return getUserInfo(userId)?.let {
      UserWithGroups(
          it,
          studyGroupRepo.getUserGroups(it.userId!!)
      )
    }
  }

  fun getUserInfoByUsername(username: String): User? {
    return userRepo.findByUsername(username)
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
        birthday = registrationUser.birthday,
        studyProgram = registrationUser.studyProgram
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

  @JvmOverloads
  fun getUsers(
      searchTerm: String? = null,
      offset: Int = 0,
      limit: Int = 10,
      currentUserId: UserId
  ): List<UserWithGroups> {
    val users = searchTerm?.let {
      userRepo.getUsers("%$it%", offset, limit, currentUserId)
    } ?: userRepo.getUsers(offset, limit, currentUserId)
    return users.map { UserWithGroups(it, studyGroupRepo.getUserGroups(it.userId!!)) }
  }

  fun getAll(): List<UserWithGroups> {
    return userRepo.findAll().map { UserWithGroups(it, studyGroupRepo.getUserGroups(it.userId!!)) }
  }

  fun updateUser(updatedUser: UpdatedUser, userId: String = AuthenticationUtils.getCurrentUserId()): User? {
    return userRepo.findByIdOrNull(userId)?.let {
      return@let User(
          userId = it.userId!!,
          username = it.username!!,
          firstName = updatedUser.firstName,
          lastName = updatedUser.lastName,
          birthday = updatedUser.birthday,
          studyProgram = updatedUser.studyProgram,
          points = updatedUser.points,
          avatar = updatedUser.avatar,
          universityId = it.universityId
      ).also { toUpdate ->
        userRepo.save(toUpdate)
      }
    }
  }
}