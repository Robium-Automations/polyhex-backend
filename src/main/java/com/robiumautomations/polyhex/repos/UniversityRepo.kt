package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.universityentities.University
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UniversityRepo : JpaRepository<University, String> {

  @Query(
      value = "SELECT * FROM universities UN " +
          "WHERE UN.university_id = (SELECT US.university_id FROM users US WHERE US.user_id = :userId );",
      nativeQuery = true
  )
  fun getUserUniversity(userId: String): University?
}