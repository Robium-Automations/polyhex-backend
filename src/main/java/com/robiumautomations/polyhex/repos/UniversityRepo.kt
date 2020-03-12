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

  @Query(
      value = "SELECT * FROM universities UN WHERE UN.university_name LIKE :queryString LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getUniversities(queryString: String, offset: Int, limit: Int): List<University>

  @Query(
      value = "SELECT * FROM universities UN WHERE UN.university_name LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getUniversities(offset: Int, limit: Int): List<University>
}