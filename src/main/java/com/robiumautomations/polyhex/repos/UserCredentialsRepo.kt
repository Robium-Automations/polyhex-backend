package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.UserCredentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserCredentialsRepo : JpaRepository<UserCredentials, String> {

  @Query(value = "SELECT * FROM user_credentials UC WHERE UC.username = :username ;", nativeQuery = true)
  fun getByUsername(@Param("username") username: String): UserCredentials?

  @Query(value = "SELECT UC.user_id FROM user_credentials UC WHERE UC.email = :email ;", nativeQuery = true)
  fun getIdByEmail(@Param("email") email: String): String?

  @Query(value = "SELECT UC.user_id FROM user_credentials UC WHERE UC.username = :username ;", nativeQuery = true)
  fun getIdByUsername(@Param("username") username: String): String?
}