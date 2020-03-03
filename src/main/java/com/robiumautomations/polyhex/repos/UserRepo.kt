package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.UserCredentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<UserCredentials, String> {

  @Query(value = "SELECT * FROM user_credentials UC WHERE UC.username = :username ;", nativeQuery = true)
  fun getByUsername(@Param("username") username: String): UserCredentials?
}