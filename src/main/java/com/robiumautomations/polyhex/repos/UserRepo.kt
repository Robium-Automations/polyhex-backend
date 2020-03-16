package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.User
import com.robiumautomations.polyhex.models.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<User, UserId> {

  @Query(
      value = "SELECT U.* FROM users U JOIN users_groups UG ON U.user_id = UG.user_id WHERE UG.study_group_id = :studyGroupId AND " +
          "U.university_id = (SELECT U1x.university_id FROM users U1 WHERE U1.user_id = :currentUserId ) ORDER BY U.lname ;",
      nativeQuery = true
  )
  fun getUsersOfGroup(studyGroupId: String, currentUserId: UserId): List<User>

  @Query(
      value = "SELECT U.* FROM users U JOIN users_groups UG ON U.user_id = UG.user_id WHERE UG.study_group_id = :studyGroupId AND " +
          "U.university_id = (SELECT U1.university_id FROM users U1 WHERE U1.user_id = :currentUserId ) AND (U.fname ILIKE :nameQuery OR U.lname ILIKE :nameQuery ) ORDER BY U.lname ;",
      nativeQuery = true
  )
  fun getUsersOfGroup(studyGroupId: String, currentUserId: UserId, nameQuery: String): List<User>
}