package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.models.UsersGroups
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UsersGroupsRepo : JpaRepository<UsersGroups, String> {

  @Query(
      value = "SELECT * FROM users_groups UG WHERE UG.study_group_id = :groupId AND UG.user_id = :userId ;",
      nativeQuery = true
  )
  fun getByUserIdAndGroupId(groupId: String, userId: UserId): UsersGroups?
}